package se.tjing.feed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import se.tjing.interaction.Interaction;
import se.tjing.membership.Membership;

@Entity
@DiscriminatorValue(InteractionNotification.INTERACTIONTYPE)
public class MembershipNotification extends Notification<Membership>{
	
	public static final String INTERACTIONTYPE="MEMBER"; //ACHTUNG

	@Override
	public String getNotificationType() {
		return INTERACTIONTYPE;
	}

	

}