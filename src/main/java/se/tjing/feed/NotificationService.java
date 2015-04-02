package se.tjing.feed;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;

import se.tjing.common.BaseEntity;
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
	
	@Autowired
	NotificationRepository notifRepo;

	public List<? extends Notification<? extends BaseEntity>> getFeed(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		QNotification notification = QNotification.notification;
		query.from(notification).where(notification.target.eq(currentUser));
		
		return query.list(notification);
	}
	
	public void killNotification(Integer notifId){
		notifRepo.delete(notifId);
		
	}
	
	public void sendNotification(Notification<?> notif){
		//TODO maybe more biznizlogic?
		notifRepo.save(notif);		
	}
}
