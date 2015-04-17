package se.tjing.feed.repositories;

import org.springframework.data.repository.CrudRepository;

import se.tjing.feed.notification.Notification;

public interface NotificationRepository extends CrudRepository<Notification, Integer>{

}
