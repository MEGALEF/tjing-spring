package se.tjing.membership;

import org.springframework.data.repository.CrudRepository;

public interface MembershipRepository extends
		CrudRepository<Membership, Integer> {
}
