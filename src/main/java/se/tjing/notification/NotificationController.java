package se.tjing.notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.common.BaseEntity;
import se.tjing.user.PersonService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
	@Autowired
	NotificationService feedService;
	
	@Autowired
	PersonService personService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<? extends Notification>> getFeed(){
		List<? extends Notification> result = feedService.getFeed(personService.getCurrentUser());
		return new ResponseEntity<List<? extends Notification>>(result, null, HttpStatus.OK);
	}
	
	@RequestMapping(value="{notifId}", method=RequestMethod.PATCH)
	public ResponseEntity<Notification> markAsRead(@PathVariable Integer notifId, @RequestBody Notification body){
		Notification result = feedService.markAsRead(body);
		return new ResponseEntity<Notification>(result, null, HttpStatus.ACCEPTED);
	}
	
}
