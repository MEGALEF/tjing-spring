package se.tjing.feed;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mysema.query.jpa.impl.JPAQuery;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.feed.notification.Notification;
import se.tjing.feed.notification.NotificationInteraction;
import se.tjing.feed.notification.NotificationMembership;
import se.tjing.feed.notification.QNotificationInteraction;
import se.tjing.feed.notification.QNotificationMembership;
import se.tjing.feed.repositories.NInteractionRepository;
import se.tjing.feed.repositories.NMembershipRepository;
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
	Facebook facebook;
	
	@Autowired
	NInteractionRepository nInteractionRepo;
	
	@Autowired
	NMembershipRepository nMembershipRepo;
	
	public void sendNotification(NotificationMembership notif, boolean notifyFacebook){
		nMembershipRepo.save(notif);
		if (notifyFacebook){
			sendFacebookNotification(notif);
		}
	}
	
	public void sendNotification(NotificationInteraction notif, boolean notifyFacebook){
		nInteractionRepo.save(notif);
		if (notifyFacebook){
			sendFacebookNotification(notif);
		}
	}

	private void sendFacebookNotification(Notification notif) {
		Person target = notif.getTarget();
		String objectId = target.getConnection().get(0).getProviderUserId();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.set("access_token", "549842908460582|ZQXpneLdsiXpC5hM8ZQxrPlMmEQ"); //TODO: Put this in .properties file
		map.set("href", "https://pure-chamber-3304.herokuapp.com/"); //TODO: Replace this
		map.set("template", notif.getMessage());
		facebook.post(objectId, "notifications", map);
	}
	

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
