package se.tjing.feed.notification;

import java.lang.reflect.Member;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import se.tjing.common.BaseEntity;
import se.tjing.itemrequest.ItemRequest;
import se.tjing.user.Person;

@Entity
public class NotificationItemRequest extends Notification<ItemRequest> {
//TODO Work in progres
	
	@OneToOne
	private ItemRequest event;
	
	public NotificationItemRequest(Person target, ItemRequest fullRequest) {
		this.target = target;
		this.event = fullRequest;
	}
	
	public NotificationItemRequest() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getNotificationType() {
		return "itemrequest";
	}

	@Override
	public ItemRequest getEvent() {
		return event;
	}
	
	@Override
	public String getMessage(){
		return event.getUser().getFirstName() + " needs a " + event.getText();
	}

}
