package se.tjing.feed.repositories;

import org.springframework.data.repository.CrudRepository;

import se.tjing.feed.notification.NotificationItemRequest;
import se.tjing.itemrequest.ItemRequest;

public interface NItemRequestRepository extends
		CrudRepository<NotificationItemRequest, Integer> {

}
