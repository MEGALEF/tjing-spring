package se.tjing.membership;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;

import se.tjing.exception.TjingException;
import se.tjing.feed.EventType;
import se.tjing.feed.Notification;
import se.tjing.feed.NotificationService;
import se.tjing.pool.Pool;
import se.tjing.pool.PoolRepository;
import se.tjing.pool.PrivacyMode;
import se.tjing.user.Person;
import se.tjing.user.PersonRepository;

@Service
public class MembershipService {

	@Autowired
	MembershipRepository membershipRepo;
	
	@Autowired
	PoolRepository poolRepo;
	
	@Autowired
	PersonRepository personRepo;	
	
	@Autowired
	NotificationService notifService;
	
	@Autowired 
	EntityManager em;

	/**
	 * Attempts to join a Pool by adding a new membership.
	 * @param membership
	 * @return
	 */
	public Membership createMembership(Person currentUser, Membership addMembership) {
		Pool pool = poolRepo.findOne(addMembership.getPool().getId());
		if (pool==null) throw new TjingException("No such pool");
		addMembership.setMember(currentUser);
		Membership result;
		
		if (findMembership(currentUser, pool) != null){
			throw new TjingException("User is already a member of that pool");
		}
		
		// If the group is closed or secret, notify users to approve. If Pool is open, preapprove membership
		if (PrivacyMode.CLOSED.equals(pool.getPrivacy())) {
			addMembership.setApproved(false);
			result = membershipRepo.save(addMembership);
			for (Membership member : pool.getApprovedMemberships()){
				notifService.sendNotification(new Notification(addMembership, member.getMember(), EventType.POOL_JOINREQUEST), true, true);
			}
			
		} else { //If PrivacyMode = OPEN
			addMembership.setApproved(true);;
			result = membershipRepo.save(addMembership);
		}
		
		return result;
	}

	private Membership findMembership(Person user, Pool pool) {
		JPAQuery query = new JPAQuery(em);
		QMembership membership = QMembership.membership;
		
		query.from(membership)
		.where(membership.member.eq(user).and(membership.pool.eq(pool)));
		
		return query.singleResult(membership);
	}

}
