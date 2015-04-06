package se.tjing.feed;

import org.springframework.data.repository.CrudRepository;

import se.tjing.feed.notification.Notification;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {

}
