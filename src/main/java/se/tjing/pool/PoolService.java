package se.tjing.pool;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.item.Item;
import se.tjing.item.QItem;
import se.tjing.membership.Membership;
import se.tjing.membership.MembershipRepository;
import se.tjing.membership.QMembership;
import se.tjing.share.QShare;
import se.tjing.user.Person;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class PoolService {
	@Autowired
	PoolRepository poolRepo;

	@Autowired
	MembershipRepository membershipRepo;

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
		// Check whether the pool need request approval from admin, member and
		// so
		// Check for preexisting memberships
		Pool pool = poolRepo.findOne(poolId);
		Membership membership = new Membership();
		membership.setMember(person);
		membership.setPool(pool);

		return membershipRepo.save(membership);
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
}
