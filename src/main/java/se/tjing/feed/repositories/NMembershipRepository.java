package se.tjing.feed.repositories;

import org.springframework.data.repository.CrudRepository;

import se.tjing.feed.notification.NotificationMembership;

public interface NMembershipRepository extends CrudRepository<NotificationMembership, Integer>{

}
