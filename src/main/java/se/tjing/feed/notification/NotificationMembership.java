package se.tjing.feed.notification;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import se.tjing.interaction.Interaction;
import se.tjing.membership.Membership;
import se.tjing.user.Person;

@Entity
public class NotificationMembership extends Notification<Membership>{
	
	@ManyToOne
	private Membership event;
	
	public NotificationMembership(Person target, Membership event, String message) {
		this.target = target;
		this.event = event;
		this.message = message;
	}
	
	public NotificationMembership() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getNotificationType() {
		return "membership";
	}

	@Override
	public Membership getEvent() {
		return event;
	}

}