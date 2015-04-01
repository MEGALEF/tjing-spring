package se.tjing.feed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import se.tjing.interaction.Interaction;
import se.tjing.user.Person;

@Entity
@DiscriminatorValue(InteractionNotification.INTERACTIONTYPE)
public class InteractionNotification extends Notification<Interaction>{
	
	public static final String INTERACTIONTYPE="INTERACTION"; //ACHTUNG

	@Override
	public String getNotificationType() {
		return INTERACTIONTYPE;
	}

}
