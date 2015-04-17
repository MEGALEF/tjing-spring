package se.tjing.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.feed.NotificationService;
import se.tjing.feed.notification.Notification;
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

	/**
	 * Attempts to join a Pool by adding a new membership.
	 * @param membership
	 * @return
	 */
	public Membership addMembership(Person currentUser, AddMembership addMembership) {
		// TODO: Moar business logic.
		Pool pool = poolRepo.findOne(addMembership.poolId);
		
		Membership membership = new Membership(currentUser, pool);
		
		// If the group is closed or secret, notify users to approve. If Pool is open, preapprove membership
		if (PrivacyMode.CLOSED.equals(pool.getPrivacy()) 
				|| PrivacyMode.SECRET.equals(pool.getPrivacy())) {
			membership.setApproved(false);
			membershipRepo.save(membership);
			for (Membership member : pool.getAprovedMemberships()){
				notifService.sendNotification(new Notification(membership, member.getMember(), "Someone applied for membership in a pool"), true);
			}
			
		} else if (PrivacyMode.OPEN.equals(pool.getPrivacy())){
			membership.approve();
			membershipRepo.save(membership);
		}
		
		return membership;
	}

}
