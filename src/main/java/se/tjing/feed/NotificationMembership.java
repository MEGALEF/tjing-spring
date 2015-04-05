package se.tjing.feed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import se.tjing.interaction.Interaction;
import se.tjing.membership.Membership;

@Entity
public class NotificationMembership extends Notification<Membership>{
	
	@ManyToOne
	private Membership event;

	@Override
	public String getNotificationType() {
		return "membership";
	}

	@Override
	public Membership getEvent() {
		return event;
	}

}