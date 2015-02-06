package se.tjing.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {

	@Autowired
	MembershipRepository membershipRepo;

	public Membership addMembership(Membership membership) {
		// TODO: Businessbusinesss
		return membershipRepo.save(membership);
	}

}
