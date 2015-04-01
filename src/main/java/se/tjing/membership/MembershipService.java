package se.tjing.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.pool.Pool;
import se.tjing.pool.PoolRepository;
import se.tjing.pool.PrivacyMode;
import se.tjing.user.Person;
import se.tjing.user.PersonRepository;
import se.tjing.user.PersonService;

@Service
public class MembershipService {

	@Autowired
	MembershipRepository membershipRepo;
	
	@Autowired
	PoolRepository poolRepo;
	
	@Autowired
	PersonRepository personRepo;	

	/**
	 * Attempts to join a Pool by adding a new membership.
	 * @param membership
	 * @return
	 */
	public Membership addMembership(Person currentUser, AddMembership addMembership) {
		// TODO: Moar business logic.
		Pool pool = poolRepo.findOne(addMembership.poolId);
		//Person user = personRepo.findOne(addMembership.userId);
		
//		if (!currentUser.equals(user)){
//			throw new TjingException("Can't add memberships for other users");
//		}
		
		Membership membership = new Membership(currentUser, pool);
		
		// If the group is closed or secret, notify users to approve. If Pool is open, preapprove membership
		if (PrivacyMode.CLOSED.equals(pool.getPrivacy()) 
				|| PrivacyMode.SECRET.equals(pool.getPrivacy())) {
			membership.setApproved(false);
			//TODO membership.setNotifyPool(pool);
		} else if (PrivacyMode.OPEN.equals(pool.getPrivacy())){
			membership.approve();
		}
		return membershipRepo.save(membership);
	}

}
