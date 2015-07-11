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
import se.tjing.membership.QMembership;
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
	SimpMessagingTemplate msgTpl;

	public Interaction accept(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(person, interaction)) {
			throw new TjingException("Only the item owner may do this");
		}
		interaction.setStatusAccepted(DateTime.now());
		
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
		Item item = interaction.getItem();
		item.setActiveInteraction(interaction);
		itemRepo.save(item);
		
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
		
		Item item = interaction.getItem();
		item.setActiveInteraction(null);
		itemRepo.save(item);
		
		sendMessage(new InteractionMessage(null, interaction.getBorrower(), ":returnconfirmed", interaction), true);
		return interactionRepo.save(interaction);
	}

	public List<Interaction> getOutgoing(Person person) {
		QInteraction interaction = QInteraction.interaction;
		QInteractionMessage message = QInteractionMessage.interactionMessage;
		JPAQuery query = new JPAQuery(em);
		
		query.from(interaction)
		.leftJoin(interaction.conversation, message)
		.where(
				interaction.borrower.eq(person)
				.and(messagesIsCurrent()
						.and(interaction.borrowerHidden.isFalse()))).distinct();
		
		return query.list(interaction);
	}
	


	/**
	 * Gets a list of a users incoming interactions, ie interactions on items
	 * owned by the user
	 */
	public List<Interaction> getIncoming(Person person) {
		QInteraction interaction = QInteraction.interaction;
		QInteractionMessage message = QInteractionMessage.interactionMessage;
		
		QItem item = QItem.item;
		JPAQuery query = new JPAQuery(em).from(interaction)
				.leftJoin(interaction.conversation, message)
				.leftJoin(interaction.item, item)
				.where(
						item.owner.eq(person)
						.and(messagesIsCurrent()
								.and(interaction.ownerHidden.isFalse()))).distinct();
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

	public List<Interaction> getUserInteractions(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		QInteraction interaction = QInteraction.interaction;
		QItem item = QItem.item;
		query.from(interaction).leftJoin(interaction.item, item)
		.where(interaction.borrower.eq(currentUser).or(item.owner.eq(currentUser)).and(interaction.deleted.isFalse()));
				//.and(interaction.active.isTrue()));
		return query.list(interaction);
	}

	public Interaction hide(Integer interactionId, Person currentUser) { //TODO: Maybe this isn't useful
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(currentUser, interaction) && !isPersonBorrower(currentUser, interaction)){
			throw new TjingException("Only parties of the interaction may do this");
		} else {
			if (interaction.getActive()){
				throw new TjingException("Can't delete an undergoing Interaction"); 
			} else {
				if (isPersonItemOwner(currentUser, interaction)) interaction.setOwnerHidden(true);
				else if (isPersonBorrower(currentUser, interaction)) interaction.setBorrowerHidden(true);
				if (interaction.getBorrowerHidden() && interaction.getOwnerHidden()){
					interaction.setDeleted(true);
					interaction.setStatusCancelled(DateTime.now());
				}
				return interactionRepo.save(interaction);
			} 
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
			sendMessage(new InteractionMessage(currentUser, item.getOwner(), createInteraction.getMessage(), interaction), false);
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

		
		
		public InteractionMessage relayMessage(Person sender, InteractionMessage msg){
			msg.setAuthor(sender);
			Interaction interaction = interactionRepo.findOne(msg.getInteraction().getId());
			if (interaction.getBorrowerHidden() || interaction.getOwnerHidden()){
				interaction.setBorrowerHidden(false);
				interaction.setOwnerHidden(false);
				interactionRepo.save(interaction);
			}
			
			if (sender.getId().equals(interaction.getBorrower().getId())){
				msg.setRecipient(interaction.getItem().getOwner());
			} else if (sender.getId().equals(interaction.getItem().getOwner().getId())){
				msg.setRecipient(interaction.getBorrower());
			} else throw new TjingException("Nope");
			
			if(timePassedSinceLastMessage(msg.getRecipient())){
				notifService.sendNotification(new Notification(msg.getInteraction(), msg.getRecipient(), EventType.INT_MESSAGE), true, true);
			}
			
			return sendMessage(msg, false);
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
		
		
		/**
		 * Private methods below
		 * 
		 */
		
		private InteractionMessage sendMessage(InteractionMessage msg, boolean systemMessage){
			msg.setSystemMessage(systemMessage);
			InteractionMessage message = msgRepo.save(msg);
			
			msgTpl.convertAndSendToUser(message.getRecipient().getUsername(), TjingURL.INTERACTION_MESSAGING_OUT, message);
			return message;
		}
		
		private BooleanExpression messagesIsCurrent(){
			QInteractionMessage message = QInteractionMessage.interactionMessage;
			
			DateTime now = DateTime.now();
			DateTime then = DateTime.now().minusWeeks(1);
			
			return message.sentTime.between(then, now)
					.or(message.read.isFalse()); 
			
		}
		
		private boolean isAccessibleToUser(Person user,
				Interaction interaction) {
			if (isPersonBorrower(user, interaction) || isPersonItemOwner(user, interaction) ){
				return true;
			} else return false;
		}
		
		private boolean timePassedSinceLastMessage(Person recipient) {
			JPAQuery query = new JPAQuery(em);
			QInteractionMessage table = QInteractionMessage.interactionMessage;
			
			query.from(table).where(table.sentTime.between(DateTime.now(), DateTime.now()
					.minusMinutes(10)).and(table.recipient.eq(recipient)));
			
			return !query.exists();
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
