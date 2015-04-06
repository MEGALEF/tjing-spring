package se.tjing.feed.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.tjing.feed.notification.NotificationInteraction;

public interface NInteractionRepository extends CrudRepository<NotificationInteraction, Integer> {

}
