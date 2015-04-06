package se.tjing.feed;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.feed.notification.Notification;
import se.tjing.feed.notification.QNotificationInteraction;
import se.tjing.feed.notification.QNotificationMembership;
import se.tjing.interaction.QInteraction;
import se.tjing.membership.QMembership;
import se.tjing.pool.PoolService;
import se.tjing.pool.QPool;
import se.tjing.user.Person;

@Service
public class NotificationService {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	PoolService poolService;
	

	public List<Notification<? extends TjingEntity>> getFeed(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		List<Notification<? extends TjingEntity>> result = new ArrayList<Notification<? extends TjingEntity>>();
		
		QNotificationMembership membnot = QNotificationMembership.notificationMembership;
				
		query.from(membnot).where(membnot.target.eq(currentUser));
		
		result.addAll(query.list(membnot));
		
		QNotificationInteraction internot = QNotificationInteraction.notificationInteraction;
		
		query.from(internot).where(internot.target.eq(currentUser));
		
		result.addAll(query.list(internot));
		
		return result;
	}
	
}
