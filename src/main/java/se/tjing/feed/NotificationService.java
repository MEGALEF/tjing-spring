package se.tjing.feed;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Service;

import se.tjing.pool.PoolService;
import se.tjing.user.Person;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class NotificationService {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	PoolService poolService;
	
	@Autowired
	Facebook facebook;
	
	@Autowired
	NotificationRepository notifRepo;
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	SimpMessagingTemplate simpTpl;
	
	public List<Notification> getFeed(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		List<Notification> result = new ArrayList<Notification>();
		
		QNotification notification = QNotification.notification;
		
		query.from(notification).where(notification.target.eq(currentUser))
		.orderBy(notification.modificationTime.desc()).limit(20);
		
		result.addAll(query.list(notification));
		
		return result;
	}

	private void sendFacebookNotification(Notification notif) {
		//Deactivated after update to Spring Social Facebook 2.0
//		Person target = notif.getTarget();
//		String objectId = target.getConnection().get(0).getProviderUserId();
//		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//		map.set("access_token", "549842908460582|ZQXpneLdsiXpC5hM8ZQxrPlMmEQ"); //TODO: Put this in .properties file
//		//map.set("href", "https://pure-chamber-3304.herokuapp.com/"); //TODO: Replace this
//		map.set("template", notif.getMessage());
//		facebook. post(objectId, "notifications", map);
	}
	
	public void sendMail(Notification notif){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(notif.getTarget().getUsername());
		message.setFrom("johannes@tribeofsales.com");
		message.setSubject("[Tjing] Notis"); //TODO
		message.setText("Det har hänt någonting på ditt Tjing-konto. Gå dit och kolla!"); //TODO
		mailSender.send(message);
	}
	
	public void sendNotification(Notification notif, boolean notifyFacebook, boolean notifyEmail){
		notifRepo.save(notif);
		if (notifyFacebook){
			sendFacebookNotification(notif);
		}
		if (notifyEmail){
			sendMail(notif);
		}
		sendWebNotification(notif);
	}
	
	public void sendWebNotification(Notification notif){
		simpTpl.convertAndSendToUser(notif.getTarget().getUsername(), "/queue/notifications/", notif);
	}
}
