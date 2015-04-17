package se.tjing.itemrequest;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.feed.NotificationService;
import se.tjing.feed.notification.Notification;
import se.tjing.pool.PoolService;
import se.tjing.user.Person;
import se.tjing.user.PersonService;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class ItemRequestService {
	
	@Autowired
	EntityManager em;
	
	@Autowired 
	ItemRequestRepository itemreqRepo;
	
	@Autowired
	NotificationService notifService;
	
	@Autowired
	PoolService poolService;
	
	@Autowired
	PersonService personService;
	
	public void postRequest(ItemRequest request){
		
	}

	public ItemRequest postRequest(Person currentUser, AddItemRequest request) {
		ItemRequest fullRequest = new ItemRequest(request, currentUser);
		itemreqRepo.save(fullRequest);
		
		notifyUserPools(fullRequest);
		
		return fullRequest;
	}

	private void notifyUserPools(ItemRequest fullRequest) {
		//TODO: Target to users pools instead once #20 is done
		List<Person> targets = personService.getVisibleUsers(fullRequest.getUser());
		
		for (Person target:targets){
			notifService.sendNotification(new Notification(fullRequest, target, "Someone needs a thing"), true);
		}
	}

	public List<ItemRequest> getUserRequests(Person currentUser) {
		QItemRequest itemreq = QItemRequest.itemRequest;
		JPAQuery query = new JPAQuery(em);
		
		query.from(itemreq).where(itemreq.member.eq(currentUser));
		
		return query.list(itemreq);
	}
}
