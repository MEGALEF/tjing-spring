package se.tjing.interaction;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import se.tjing.TjingURL;
import se.tjing.exception.TjingException;
import se.tjing.interactionmessage.InteractionMessage;
import se.tjing.interactionmessage.InteractionMessageRepository;
import se.tjing.interactionmessage.InteractionMessageService;
import se.tjing.interactionmessage.QInteractionMessage;
import se.tjing.item.Item;
import se.tjing.item.ItemRepository;
import se.tjing.item.ItemService;
import se.tjing.item.QItem;
import se.tjing.membership.QMembership;
import se.tjing.notification.EventType;
import se.tjing.notification.Notification;
import se.tjing.notification.NotificationService;
import se.tjing.user.Person;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;

@Service
public class InteractionService {

	@Autowired
	EntityManager em;

	@Autowired
	InteractionRepository interactionRepo;

	@Autowired
	InteractionMessageRepository msgRepo;

	@Autowired
	ItemService itemService;

	@Autowired
	ItemRepository itemRepo;

	@Autowired
	NotificationService notifService;

	@Autowired
	InteractionMessageService msgService;

	@Autowired
	SimpMessagingTemplate msgTpl;

	public Interaction accept(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(person, interaction)) {
			throw new TjingException("Only the item owner may do this");
		}
		interaction.setStatusAccepted(DateTime.now());

		interaction = interactionRepo.save(interaction);
		notifService.sendNotification(new Notification(interaction, interaction.getBorrower(), EventType.INT_ACCEPT), true, true);
		msgService.sendMessage(new InteractionMessage(null, interaction.getBorrower(), ":requestaccepted", interaction), true);
		return interaction;
	}

	public Interaction confirmHandover(Integer interactionId, Person user) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonBorrower(user, interaction)) {
			throw new TjingException(
					"Only the user who initially sent the request may do this");
		}
		Item item = interaction.getItem();
		item.setActiveInteraction(interaction);
		itemRepo.save(item);

		interaction.setStatusHandedOver(DateTime.now());
		interaction = interactionRepo.save(interaction);
		notifService.sendNotification(new Notification(interaction, interaction.getItem().getOwner(), EventType.INT_HAND), true, true);
		msgService.sendMessage(new InteractionMessage(null, interaction.getItem().getOwner(), ":handoverconfirmed", interaction), true);
		return interaction;
	}

	public Interaction confirmReturn(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(person, interaction)) {
			throw new TjingException("Only the item owner may do this");
		}
		interaction.setStatusReturned(DateTime.now());

		notifService.sendNotification(new Notification(interaction, interaction.getBorrower(), EventType.INT_RET), true, true);

		Item item = interaction.getItem();
		item.setActiveInteraction(null);
		itemRepo.save(item);

		msgService.sendMessage(new InteractionMessage(null, interaction.getBorrower(), ":returnconfirmed", interaction), true);
		return interactionRepo.save(interaction);
	}

	public Interaction getInteraction(Person user,
			Integer interactionId) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (interaction.getConversation()!=null){
			for (InteractionMessage msg : interaction.getConversation()){
				if (msg.getRecipient().getId() == user.getId()){
					msg.setRead(true);
					msgRepo.save(msg);
				}
			}
		}

		if (isAccessibleToUser(user, interaction)){
			return interaction;
		} else {
			throw new TjingException("Not accessible");
		}
	}

	public List<Interaction> getOutgoing(Person person) {
		QInteraction interaction = QInteraction.interaction;
		QInteractionMessage message = QInteractionMessage.interactionMessage;
		JPAQuery query = new JPAQuery(em);

		query.from(interaction)
		.leftJoin(interaction.conversation, message)
		.where(
				interaction.borrower.eq(person)
				.and(msgService.messagesIsCurrent()
						.and(interaction.borrowerHidden.isFalse()))).distinct();

		return query.list(interaction);
	}

	public List<Interaction> getIncoming(Person person) {
		QInteraction interaction = QInteraction.interaction;
		QInteractionMessage message = QInteractionMessage.interactionMessage;

		QItem item = QItem.item;
		JPAQuery query = new JPAQuery(em).from(interaction)
				.leftJoin(interaction.conversation, message)
				.leftJoin(interaction.item, item)
				.where(
						item.owner.eq(person)
						.and(msgService.messagesIsCurrent()
								.and(interaction.ownerHidden.isFalse()))).distinct();
		return query.list(interaction);
	}




	public List<Interaction> getUserInteractions(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		QInteraction interaction = QInteraction.interaction;
		QItem item = QItem.item;
		query.from(interaction).leftJoin(interaction.item, item)
		.where(interaction.borrower.eq(currentUser).or(item.owner.eq(currentUser)).and(interaction.deleted.isFalse()));
		//.and(interaction.active.isTrue()));
		return query.list(interaction);
	}

	public Interaction deny(Integer interactionId, Person user) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (interaction.getStatusAccepted() != null) {
			throw new TjingException(
					"The interaction has already been accepted.");
		}
		return this.hide(interactionId, user);
	}

	public Interaction hide(Integer interactionId, Person currentUser) { //TODO: Maybe this isn't useful
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(currentUser, interaction) && !isPersonBorrower(currentUser, interaction)){
			throw new TjingException("Only parties of the interaction may do this");
		} 
		if (interaction.isActive()){
			throw new TjingException("Can't delete an ongoing Interaction");
		}
		else {
			if (isPersonItemOwner(currentUser, interaction))
				interaction.setOwnerHidden(true);
			else interaction.setBorrowerHidden(true);
			if (interaction.getBorrowerHidden() && interaction.getOwnerHidden()){
				interaction.setDeleted(true);
				interaction.setStatusCancelled(DateTime.now());
			}

			for(InteractionMessage msg: interaction.getConversation()){
				if (msg.getRecipient().getId().equals(currentUser.getId()) && msg.getRead()==false){
					msg.setRead(true);
					msgRepo.save(msg);
				}
			}
			return interactionRepo.save(interaction);
		} 
	}


	public Interaction initiateRequest(Person currentUser, AddInteraction createInteraction) {
		Item item = itemRepo.findOne(createInteraction.getItemId());
		if (item == null || !itemService.isItemVisibleToUser(currentUser, item)) {
			throw new TjingException(
					"Item does not exist or is not available to you");
		}
		Interaction interaction = new Interaction(currentUser, item, DateTime.now());

		interactionRepo.save(interaction);
		msgService.sendMessage(new InteractionMessage(currentUser, item.getOwner(), createInteraction.getMessage(), interaction), false);
		notifService.sendNotification(new Notification(interaction, interaction.getItem().getOwner(), EventType.INT_REQUEST), true, true);
		return interaction;
	}


	/**
	 * Private methods below
	 * 
	 */



	private boolean isAccessibleToUser(Person user,
			Interaction interaction) {
		if (isPersonBorrower(user, interaction) || isPersonItemOwner(user, interaction) ){
			return true;
		} else return false;
	}



	private boolean isPersonItemOwner(Person p, Interaction i) {
		if (!p.getId().equals(i.getItem().getOwner().getId())) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isPersonBorrower(Person p, Interaction i) {
		if (!p.getId().equals(i.getBorrower().getId())) {
			return false;
		} else {
			return true;
		}
	}

}
