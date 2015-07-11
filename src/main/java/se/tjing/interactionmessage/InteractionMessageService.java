package se.tjing.interactionmessage;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import se.tjing.TjingURL;
import se.tjing.exception.TjingException;
import se.tjing.interaction.Interaction;
import se.tjing.interaction.InteractionRepository;
import se.tjing.interaction.InteractionService;
import se.tjing.interaction.QInteraction;
import se.tjing.notification.EventType;
import se.tjing.notification.Notification;
import se.tjing.notification.NotificationService;
import se.tjing.user.Person;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;

@Service
public class InteractionMessageService {
	
	@Autowired
	InteractionRepository interactionRepo;
	
	@Autowired
	InteractionMessageRepository msgRepo;
	
	@Autowired
	NotificationService notifService;
	
	@Autowired
	InteractionService interactionService;
	
	@Autowired
	EntityManager em;
	
	@Autowired
	SimpMessagingTemplate msgTpl;
	
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
		
		boolean sendNotif = !recentMessages(msg.getRecipient());
		msg = msgRepo.save(msg);
		if(sendNotif){
			notifService.sendNotification(new Notification(msg, msg.getRecipient(), EventType.INT_MESSAGE), true, true);
		}
		
		return sendMessage(msg, false);
	}

	public List<InteractionMessage> getUnread(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		QInteractionMessage messages = QInteractionMessage.interactionMessage;
		QInteraction interaction = QInteraction.interaction;
		
		query.from(messages).leftJoin(messages.interaction, interaction)
		.where(messages.recipient.eq(currentUser).and(messages.read.isFalse()));
		
		return query.list(messages);
	}

	public List<InteractionMessage> getConversation(Person user,
			Integer interactionId) {
		Interaction interaction = interactionService.getInteraction(user, interactionId);
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
	
	public InteractionMessage sendMessage(InteractionMessage msg, boolean systemMessage){
		msg.setSystemMessage(systemMessage);
		InteractionMessage message = msgRepo.save(msg);
		
		msgTpl.convertAndSendToUser(message.getRecipient().getUsername(), TjingURL.INTERACTION_MESSAGING_OUT, message);
		return message;
	}
	
	public BooleanExpression messagesIsCurrent(){
		QInteractionMessage message = QInteractionMessage.interactionMessage;
		
		DateTime now = DateTime.now();
		DateTime then = DateTime.now().minusWeeks(1);
		
		return message.sentTime.between(then, now)
				.or(message.read.isFalse()); 
		
	}
	private boolean recentMessages(Person recipient) {
		JPAQuery query = new JPAQuery(em);
		QInteractionMessage table = QInteractionMessage.interactionMessage;
		
		DateTime now = DateTime.now();
		DateTime then = now.minusMinutes(10);
		
		query.from(table)
		.where(table.recipient.eq(recipient).and(table.sentTime.after(then)));
		
		boolean result = query.exists();
		return result;
		}

}
