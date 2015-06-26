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
	public Membership addMembership(Person currentUser, AddMembership addMembership) {
		Pool pool = poolRepo.findOne(addMembership.poolId);
		
		if (findMembership(currentUser, pool) != null){
			throw new TjingException("User is already a member of that pool");
		}
		
		Membership membership = new Membership(currentUser, pool);
		
		// If the group is closed or secret, notify users to approve. If Pool is open, preapprove membership
		if (PrivacyMode.CLOSED.equals(pool.getPrivacy()) 
				|| PrivacyMode.SECRET.equals(pool.getPrivacy())) {
			membership.setApproved(false);
			membershipRepo.save(membership);
			for (Membership member : pool.getApprovedMemberships()){
				notifService.sendNotification(new Notification(membership, member.getMember(), EventType.POOL_JOINREQUEST), true, true);
			}
			
		} else if (PrivacyMode.OPEN.equals(pool.getPrivacy())){
			membership.approve();
			membershipRepo.save(membership);
		}
		
		return membership;
	}

	private Membership findMembership(Person currentUser, Pool pool) {
		JPAQuery query = new JPAQuery(em);
		QMembership membership = QMembership.membership;
		
		query.from(membership).where(membership.member.eq(currentUser).and(membership.pool.eq(pool)));
		
		return query.singleResult(membership);
	}

}
