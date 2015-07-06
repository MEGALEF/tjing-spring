package se.tjing.interaction;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import se.tjing.TjingURL;
import se.tjing.exception.TjingException;
import se.tjing.feed.EventType;
import se.tjing.feed.Notification;
import se.tjing.feed.NotificationService;
import se.tjing.interactionmessage.InteractionMessage;
import se.tjing.interactionmessage.InteractionMessageRepository;
import se.tjing.interactionmessage.QInteractionMessage;
import se.tjing.item.Item;
import se.tjing.item.ItemRepository;
import se.tjing.item.ItemService;
import se.tjing.item.QItem;
import se.tjing.user.Person;

import com.mysema.query.jpa.impl.JPAQuery;

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
	SimpMessagingTemplate msgTpl;

	public Interaction accept(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(person, interaction)) {
			throw new TjingException("Only the item owner may do this");
		}
		interaction.setStatusAccepted(DateTime.now());
		
		Item item = interaction.getItem();
		item.setActiveInteraction(interaction);
		itemRepo.save(item);
		interaction = interactionRepo.save(interaction);
		notifService.sendNotification(new Notification(interaction, interaction.getBorrower(), EventType.INT_ACCEPT), true, true);
		sendMessage(new InteractionMessage(null, interaction.getBorrower(), ":requestaccepted", interaction), true);
		return interaction;
	}

	public Interaction confirmHandover(Integer interactionId, Person user) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonBorrower(user, interaction)) {
			throw new TjingException(
					"Only the user who initially sent the request may do this");
		}
		//TODO interaction.setNotifyUser(interaction.getItem().getOwner());
		interaction.setStatusHandedOver(DateTime.now());
		interaction = interactionRepo.save(interaction);
		notifService.sendNotification(new Notification(interaction, interaction.getItem().getOwner(), EventType.INT_HAND), true, true);
		sendMessage(new InteractionMessage(null, interaction.getItem().getOwner(), ":handoverconfirmed", interaction), true);
		return interaction;
	}

	public Interaction confirmReturn(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(person, interaction)) {
			throw new TjingException("Only the item owner may do this");
		}
		interaction.setStatusReturned(DateTime.now());

		notifService.sendNotification(new Notification(interaction, interaction.getBorrower(), EventType.INT_RET), true, true);
		interaction.getItem().setActiveInteraction(null);
		interaction.setActive(false);
		sendMessage(new InteractionMessage(null, interaction.getBorrower(), ":returnconfirmed", interaction), true);
		return interactionRepo.save(interaction);
	}

	public List<Interaction> getOutgoing(Person person) {
		QInteraction interaction = QInteraction.interaction;
		JPAQuery query = new JPAQuery(em);
		query.from(interaction).where(
				interaction.borrower.eq(person));
						//.and(interaction.active.isTrue()));
		return query.list(interaction);
	}

	/**
	 * Gets a list of a users incoming interactions, ie interactions on items
	 * owned by the user
	 */
	public List<Interaction> getUserIncomingInteractions(Person person) {
		QInteraction interaction = QInteraction.interaction;
		QItem item = QItem.item;
		JPAQuery query = new JPAQuery(em).from(interaction)
				.leftJoin(interaction.item, item)
				.where(item.owner.eq(person));
						//.and(interaction.active.isTrue()));
		return query.list(interaction);
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

	public Interaction deny(Integer interactionId, Person currentUser) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(currentUser, interaction)) {
			throw new TjingException("Only the owner of the item may do this");
		}
		if (interaction.getStatusAccepted() != null) {
			throw new TjingException(
					"The interaction has already been accepted.");
		}
		interaction.setActive(false);
		return interactionRepo.save(interaction);
	}

	public List<Interaction> getUserInteractions(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		QInteraction interaction = QInteraction.interaction;
		QItem item = QItem.item;
		query.from(interaction).leftJoin(interaction.item, item)
		.where(interaction.borrower.eq(currentUser).or(item.owner.eq(currentUser)));
				//.and(interaction.active.isTrue()));
		return query.list(interaction);
	}

	public Interaction cancel(Integer interactionId, Person currentUser) { //TODO: Maybe this isn't useful
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(currentUser, interaction) && !isPersonBorrower(currentUser, interaction)){
			throw new TjingException("Only parties of the interaction may do this");
		} else {
			interaction.setStatusCancelled(DateTime.now());
			interaction.setActive(false);
			notifService.sendNotification(new Notification(interaction, interaction.getBorrower(), EventType.INT_CANCEL), true, true);
			return interaction;
		}
		//TODO Cancel interactions even when item is handed over? Ok or not?
	}
	
		public Interaction initiateRequest(Person currentUser, AddInteraction createInteraction) {
			Item item = itemRepo.findOne(createInteraction.getItemId());
			if (item == null || !itemService.isItemVisibleToUser(currentUser, item)) {
				throw new TjingException(
						"Item does not exist or is not available to you");
			}
			Interaction interaction = new Interaction(currentUser, item, DateTime.now());
			interactionRepo.save(interaction);
			notifService.sendNotification(new Notification(interaction, interaction.getItem().getOwner(), EventType.INT_REQUEST), true, true);
			return interaction;
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

		private boolean isAccessibleToUser(Person user,
				Interaction interaction) {
			if (isPersonBorrower(user, interaction) || isPersonItemOwner(user, interaction) ){
				return true;
			} else return false;
		}
		
		public InteractionMessage relayMessage(Person sender, InteractionMessage msg){
			msg.setAuthor(sender);
			Interaction interaction = interactionRepo.findOne(msg.getInteraction().getId());
			
			if (sender.getId().equals(interaction.getBorrower().getId())){
				msg.setRecipient(interaction.getItem().getOwner());
			} else if (sender.getId().equals(interaction.getItem().getOwner().getId())){
				msg.setRecipient(interaction.getBorrower());
			} else throw new TjingException("Nope");
			
			return sendMessage(msg, false);
		}
		
		private InteractionMessage sendMessage(InteractionMessage msg, boolean systemMessage){
			msg.setSystemMessage(systemMessage);
			InteractionMessage message = msgRepo.save(msg);
			
			msgTpl.convertAndSendToUser(message.getRecipient().getUsername(), TjingURL.INTERACTION_MESSAGING_OUT, message);
			return message;
		}

		public List<InteractionMessage> getUnread(Person currentUser) {
			JPAQuery query = new JPAQuery(em);
			QInteractionMessage messages = QInteractionMessage.interactionMessage;
			
			query.from(messages)
			.where(messages.recipient.eq(currentUser).and(messages.read.isFalse()));
			
			return query.list(messages);
		}

		public List<InteractionMessage> getConversation(Person user,
				Integer interactionId) {
			Interaction interaction = getInteraction(user, interactionId);
			List<InteractionMessage> conversation = interaction.getConversation();
			for (InteractionMessage message : conversation){
				if (message.getRecipient().getId().equals(user.getId())){
					message.setRead(true);
					msgRepo.save(message);
				}
			}
			return interaction.getConversation();
		}

		public InteractionMessage updateMessage(Person currentUser, InteractionMessage update) {
			InteractionMessage msg = msgRepo.findOne(update.getId());
			if (msg==null) throw new TjingException("No such message");
			
			if(msg.getRecipient().getId().equals(currentUser.getId())){
				if(update.getRead() == true) msg.setRead(true);
				return msgRepo.save(msg);
			} return msg;			
		}
}
