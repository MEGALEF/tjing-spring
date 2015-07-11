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
import se.tjing.pool.PoolRole;
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

	@Autowired
	NotificationService notifService;

	@Autowired
	PersonService personService;

	@Autowired 
	EntityManager em;

	private void checkForExistingMembership(Person user, Pool pool){
		if (findMembership(user, pool) != null){
			throw new TjingException("User is already a member of that pool");
		}
	}
	public Membership createMembership(Person currentUser, PostMembership addMembership) {
		Pool pool = poolRepo.findOne(addMembership.getPool().getId());
		Membership add = new Membership();
		
		
		if (pool==null) throw new TjingException("No such pool");
		
		if (addMembership.getMember()!= null && addMembership.getMember().getUsername() != null){
			Membership adderMembership = findMembership(currentUser, pool);
			
			if(adderMembership==null) throw new TjingException("Can't invite others if you're not a member");
			if(adderMembership.getMember().getId()==addMembership.getMember().getId()) throw new TjingException("Stop trying to add yourself, dummy");
			
			Person user = personService.getPersonByEmail(addMembership.getMember().getUsername());
			if (adderMembership.getRole().equals(PoolRole.ADMIN) || pool.getApproval().equals(PoolRole.MEMBER)){
				checkForExistingMembership(user, pool);
				add = new Membership(user, pool, true, PoolRole.MEMBER);
				return membershipRepo.save(add);
			} else {
				throw new TjingException("You don't have rights to add new members");
			}
		} else {
			add.setPool(pool);
			add.setMember(currentUser);
			Membership result;

			checkForExistingMembership(currentUser, pool);

			// If the group is closed or secret, notify users to approve. If Pool is open, preapprove membership
			if (PrivacyMode.CLOSED.equals(pool.getPrivacy())) {
				add.setApproved(false);
				result = membershipRepo.save(add);
				for (Membership member : pool.getApprovedMemberships()){
					notifService.sendNotification(new Notification(add, member.getMember(), EventType.POOL_JOINREQUEST), true, true);
				}

			} else { //If PrivacyMode = OPEN
				add.setApproved(true);;
				result = membershipRepo.save(add);
			}

			return result;

		}
	}

	private Membership findMembership(Person user, Pool pool) {
		JPAQuery query = new JPAQuery(em);
		QMembership membership = QMembership.membership;

		query.from(membership)
		.where(membership.member.eq(user).and(membership.pool.eq(pool)));

		return query.singleResult(membership);
	}

}
