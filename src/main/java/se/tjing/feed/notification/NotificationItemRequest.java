package se.tjing.feed.notification;

import javax.persistence.Entity;

import se.tjing.common.BaseEntity;
import se.tjing.itemrequest.ItemRequest;
import se.tjing.user.Person;

@Entity
public class NotificationItemRequest extends Notification<ItemRequest> {
//TODO Work in progress
	private Integer id;
	
	private ItemRequest event;
	
	public NotificationItemRequest(Person target, ItemRequest fullRequest) {
		this.target = target;
		this.event = fullRequest;
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

}
