package se.tjing.pool;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.item.Item;
import se.tjing.item.QItem;
import se.tjing.membership.JoinRequest;
import se.tjing.membership.JoinRequestRepository;
import se.tjing.membership.Membership;
import se.tjing.membership.MembershipRepository;
import se.tjing.membership.QJoinRequest;
import se.tjing.membership.QMembership;
import se.tjing.share.QShare;
import se.tjing.share.Share;
import se.tjing.share.ShareRepository;
import se.tjing.user.Person;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class PoolService {
	@Autowired
	PoolRepository poolRepo;

	@Autowired
	MembershipRepository membershipRepo;

	@Autowired
	ShareRepository shareRepo;

	@Autowired
	JoinRequestRepository requestRepo;

	@Autowired
	EntityManager em;

	public Pool addPool(Person creator, Pool pool) {
		if (pool.getTitle().trim().isEmpty()) {
			throw new TjingException("A pool must have a title");
		}
		List<Pool> sameNamePools = poolRepo.findByTitle(pool.getTitle());
		if (sameNamePools.size() > 0) {
			throw new TjingException("Title of a pool must be unique");
		}

		// Set pool creator as member in the pool
		Membership creatorMembership = new Membership();
		creatorMembership.setMember(creator);
		creatorMembership.setPool(pool);
		Pool savedPool = poolRepo.save(pool);
		membershipRepo.save(creatorMembership);
		return savedPool;
	}

	public Membership joinPool(Person person, Integer poolId) {
		// TODO: Business logic.
		// Check for preexisting memberships
		Pool pool = poolRepo.findOne(poolId);

		// If the group is closed or secret, a request is created
		if (pool.getPrivacy() == PrivacyMode.CLOSED
				|| pool.getPrivacy() == PrivacyMode.SECRET) {
			JoinRequest request = new JoinRequest(person, pool);
			requestRepo.save(request);
			return null;

		} else {
			Membership membership = new Membership();
			membership.setMember(person);
			membership.setPool(pool);

			return membershipRepo.save(membership);
		}
	}

	public List<Pool> getPools() {
		// TODO: Limit to user visible pools. Business Logic
		QPool pool = QPool.pool;
		JPAQuery query = new JPAQuery(em);
		query.from(pool);
		return query.list(pool);
	}

	public List<Item> getItemsInPool(Person user, Integer poolId) {
		Pool pool = poolRepo.findOne(poolId);
		if (pool == null || !isUserMemberOfPool(user, pool)) {
			throw new TjingException(
					"Pool does not exist or user is not allowed access to it");
		} else {
			QItem item = QItem.item;
			QShare share = QShare.share;
			JPAQuery query = new JPAQuery(em);
			query.from(item).leftJoin(item.shares, share)
					.where(share.pool.eq(pool));
			return query.list(item);
		}
	}

	private Boolean isUserMemberOfPool(Person person, Pool pool) {
		QMembership membership = QMembership.membership;
		JPAQuery query = new JPAQuery(em);
		query.from(membership).where(
				membership.pool.eq(pool).and(membership.member.eq(person)));
		return query.exists();
	}

	public List<Pool> getUsersPools(Person user) {
		QMembership membership = QMembership.membership;
		QPool pool = QPool.pool;
		JPAQuery query = new JPAQuery(em);
		query.from(pool).leftJoin(pool.memberships, membership)
				.where(membership.member.eq(user));
		return query.list(pool);
	}

	public Pool getPool(Integer poolId) {
		Pool pool = poolRepo.findOne(poolId);
		if (pool == null) {
			throw new TjingException("No such pool");
		}
		return pool;
	}

	public List<Pool> search(String searchString) {
		QPool pool = QPool.pool;
		JPAQuery query = new JPAQuery(em);
		query.from(pool).where(pool.title.containsIgnoreCase(searchString));
		return query.list(pool);
	}

	public List<Item> getOwnedItemsInPool(Person currentUser, Integer poolId) {
		QItem item = QItem.item;
		QShare share = QShare.share;
		Pool pool = poolRepo.findOne(poolId);
		if (!isUserMemberOfPool(currentUser, pool)) {
			throw new TjingException("User is not a member of this pool");
		}
		JPAQuery query = new JPAQuery(em);
		query.from(item).leftJoin(item.shares, share)
				.where(item.owner.eq(currentUser).and(share.pool.eq(pool)));
		return query.list(item);
	}

	public List<Membership> leavePool(Person currentUser, Integer poolId) {
		Pool pool = poolRepo.findOne(poolId);

		// Remove users shares to pool
		QShare share = QShare.share;
		QItem item = QItem.item;
		JPAQuery sharesQuery = new JPAQuery(em);
		sharesQuery.from(share).leftJoin(share.item, item)
				.where(item.owner.eq(currentUser).and(share.pool.eq(pool)));
		List<Share> shares = sharesQuery.list(share);
		for (Share shareToDelete : shares) {
			shareRepo.delete(shareToDelete);
		}

		// Remove users membership in pool
		QMembership membership = QMembership.membership;
		JPAQuery membershipQuery = new JPAQuery(em);
		membershipQuery.from(membership)
				.where(membership.member.eq(currentUser).and(
						membership.pool.eq(pool)));
		Membership membershipToDelete = membershipQuery
				.singleResult(membership);
		membershipRepo.delete(membershipToDelete);

		// Return list of users remaining memberships
		return currentUser.getMemberships();
	}

	public List<JoinRequest> getRelevantRequests(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		QJoinRequest request = QJoinRequest.joinRequest;
		QMembership membership = QMembership.membership;
		QPool pool = QPool.pool;
		// Get requests to groups in which the user is a member
		query.from(request).leftJoin(request.pool, pool)
				.leftJoin(pool.memberships, membership)
				.where(membership.member.eq(currentUser));

		return query.list(request);
	}

	public Membership approveJoin(Person user, Integer requestId) {
		JoinRequest req = requestRepo.findOne(requestId);
		if (!isUserMemberOfPool(user, req.getPool())) {
			throw new TjingException("Only pool members may do this");
		} else {
			requestRepo.delete(req);
			Membership membership = new Membership();
			membership.setMember(req.getMember());
			membership.setPool(req.getPool());

			return membershipRepo.save(membership);
		}
	}

	public boolean denyJoin(Person user, Integer requestId) {
		JoinRequest req = requestRepo.findOne(requestId);
		if (!isUserMemberOfPool(user, req.getPool())) {
			throw new TjingException("Only pool members may do this");
		} else {
			requestRepo.delete(req);
			return true;
		}
	}
	// TODO: create private getPool(poolId) method
}
