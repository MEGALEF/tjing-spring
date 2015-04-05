package se.tjing.feed;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface NInteractionRepository extends CrudRepository<NotificationInteraction, Integer> {

}
