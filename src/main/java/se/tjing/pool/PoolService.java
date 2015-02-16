package se.tjing.pool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.membership.Membership;
import se.tjing.membership.MembershipRepository;
import se.tjing.user.Person;

@Service
public class PoolService {
	@Autowired
	PoolRepository poolRepo;

	@Autowired
	MembershipRepository membershipRepo;

	public Pool addPool(Pool pool) {
		// TODO: Business logic I guess
		return poolRepo.save(pool);
	}

	public Membership joinPool(Person person, Integer poolId) {
		// TODO: Business logic.
		// Check whether the pool need request approval from admin, member and
		// so
		// Check for preexisting memberships
		Pool pool = poolRepo.findOne(poolId);
		Membership membership = new Membership();
		membership.setMember(person);
		membership.setPool(pool);

		return membershipRepo.save(membership);
	}

}
