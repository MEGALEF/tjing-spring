package se.tjing.feed.notification;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import se.tjing.interaction.Interaction;
import se.tjing.user.Person;

@Entity
public class NotificationInteraction extends Notification<Interaction>{

	@ManyToOne
	private Interaction event;
	
	public NotificationInteraction(Interaction event, Person target, String message) {
		this.event = event;
		this.target = target;
		this.message = message;
	}
	
	public NotificationInteraction() {
	}
	
	@Override
	public String getNotificationType() {
		return "interaction";
	}

	@Override
	public Interaction getEvent() {
		return event;
	}

	

}
