package se.tjing.pool;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.GroupMembership;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.item.Item;
import se.tjing.item.QItem;
import se.tjing.membership.Membership;
import se.tjing.membership.MembershipRepository;
import se.tjing.membership.QMembership;
import se.tjing.share.QShare;
import se.tjing.share.Share;
import se.tjing.share.ShareRepository;
import se.tjing.user.Person;
import se.tjing.user.QPerson;

import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;

@Service
public class PoolService {
	@Autowired
	PoolRepository poolRepo;

	@Autowired
	MembershipRepository membershipRepo;

	@Autowired
	ShareRepository shareRepo;

	@Autowired
	EntityManager em;

	@Autowired
	Facebook facebook;

	public Pool createPool(Person creator, Pool pool) {
		if (pool.getTitle().trim().isEmpty()) {
			throw new TjingException("A pool must have a title");
		}
		List<Pool> sameNamePools = poolRepo.findByTitle(pool.getTitle());
		if (sameNamePools.size() > 0) {
			throw new TjingException("Title of a pool must be unique");
		}

		// Set pool creator as member in the pool
		Membership creatorMembership = new Membership(creator, pool);
		creatorMembership.approve();
		creatorMembership.setRole(PoolRole.ADMIN);

		Pool savedPool = poolRepo.save(pool);
		membershipRepo.save(creatorMembership);
		return savedPool;
	}



	public List<Pool> getPools() {
		// TODO: Limit to user visible pools. Business Logic
		QPool pool = QPool.pool;
		JPAQuery query = new JPAQuery(em);
		query.from(pool).where(pool.privacy.ne(PrivacyMode.SECRET));
		return query.list(pool);
	}

	public List<Share> getPoolShares(Person user, Integer poolId) {
		Pool pool = poolRepo.findOne(poolId);
		if (pool == null || !isUserMemberOfPool(user, pool)) {
			throw new TjingException(
					"Pool does not exist or user is not allowed access to it");
		} else {
			QShare share = QShare.share;
			JPAQuery query = new JPAQuery(em);
			query.from(share)
			.where(share.pool.eq(pool));
			return query.list(share);
		}
	}

	private Boolean isUserMemberOfPool(Person person, Pool pool) {
		QMembership membership = QMembership.membership;
		JPAQuery query = new JPAQuery(em);
		query.from(membership).where(
				membership.pool.eq(pool).and(membership.member.eq(person).and(membership.approved.isTrue())));
		return query.exists();
	}

	public List<Pool> getUsersPools(Person user) {
		QMembership membership = QMembership.membership;
		QPool pool = QPool.pool;
		JPAQuery query = new JPAQuery(em);
		query.from(pool).leftJoin(pool.memberships, membership)
		.where(membership.member.eq(user).and(membership.approved.isTrue()));
		return query.list(pool);
	}

	public Pool getPool(Integer poolId) {
		Pool pool = poolRepo.findOne(poolId);
		if (pool == null) {
			throw new TjingException("No such pool");
		}
		return pool;
	}

	public List<Pool> searchPools(String searchString) {
		QPool pool = QPool.pool;
		JPAQuery query = new JPAQuery(em);
		query.from(pool).where(pool.title.containsIgnoreCase(searchString).and(pool.privacy.ne(PrivacyMode.SECRET)));
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

	public List<Membership> getPendingMemberships(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		QMembership membership = QMembership.membership;
		// Get requests to groups in which the user is a member

		query.from(membership)
		.where(membership.approved.isFalse().and(membership.pool.in(this.getUsersPools(currentUser))));

		return query.list(membership);
	}

	public Membership update(Person user, Integer requestId, Membership update) {
		Membership old = membershipRepo.findOne(requestId);
		if (old == null) throw new TjingException("No such entity");
		
		if (update.getRole() != null){
			if (PoolRole.ADMIN.equals(getMembership(user, old.getPool()).getRole())){
				old.setRole(update.getRole());
			} else throw new TjingException("Only admins may do this");
		}		

		//TODO: Check user role vs Pool rules (approval)
		if (!isUserMemberOfPool(user, old.getPool())) {
			throw new TjingException("Only pool members may do this");
		} else {
			old.approve();
		}
		
		return membershipRepo.save(old);
	}

	private Membership getMembership(Person user, Pool pool) {
		JPAQuery query = new JPAQuery(em);
		QMembership membership = QMembership.membership;
		query.from(membership)
		.where(membership.member.eq(user)
				.and(membership.pool.eq(pool)));
		return query.singleResult(membership);
	}



	public void removeMembership(Person user, Integer membershipId) {
		Membership membership = membershipRepo.findOne(membershipId);
		if (membership == null) throw new TjingException("No such Membership");
		Pool pool = membership.getPool();
		if (membership.getApproved()){ //Member is an approved member of the pool
			if (!user.equals(membership.getMember())){
				Membership userM = getMembership(user, pool);
				if (userM != null && PoolRole.ADMIN.equals(userM.getRole())){
					deleteMembershipAndShares(membership);
				} else throw new TjingException("Only admins may remove other users");
			} else {
				deleteMembershipAndShares(membership);
			}
		} else {
			if (!isUserMemberOfPool(user, membership.getPool())) {
				//TODO Here is for implementation of pool role approval
				throw new TjingException("Only pool members may do this");
			} else {
				deleteMembershipAndShares(membership);
			}
		}
		// Delete empty pools
		if (pool.getMemberships().isEmpty()){
			poolRepo.delete(pool);
		}
	}
	// TODO: create private getPool(poolId) method.
	// ^ Why?



	private void deleteMembershipAndShares(Membership membership) {
		membershipRepo.delete(membership);
		removeShares(membership.getMember(), membership.getPool());
	}

	private void removeShares(Person member, Pool pool) {
		QShare share = QShare.share;
		QItem item = QItem.item;
		JPAQuery query = new JPAQuery(em);

		query.from(share).leftJoin(share.item, item).where(share.pool.eq(pool).and(item.owner.eq(member)));

		for (Share s : query.list(share)){
			shareRepo.delete(s);
		}
	}

	public List<Membership> getUserMemberships(Person user) {
		JPAQuery query = new JPAQuery(em);
		QMembership membership = QMembership.membership;
		QPool pool = QPool.pool;

		OrderSpecifier<Integer> order = new OrderSpecifier<Integer>(Order.DESC, pool.memberships.size());

		query.from(membership).leftJoin(membership.pool, pool)
		.where(membership.member.eq(user).and(membership.approved.isTrue())).orderBy(order);

		return query.list(membership);
	}

	public List<GroupMembership> getFacebookGroups(Person currentUser) {
		List<GroupMembership> fbmemberships;
		if (!facebook.isAuthorized()){
			throw new TjingException("You must use login via facebook to access this feature");
		}else {
			fbmemberships = facebook.groupOperations().getMemberships();

			/*for (GroupMembership gm : fbmemberships){
				Pool pool = new Pool(gm);

				result.add(pool);

				Long id = pool.getFacebookId();
				List<Pool> existing = poolRepo.findByFacebookId(id);


			}*/
		}
		return fbmemberships;
	}



	public Membership importFbGroup(Person currentUser, FacebookGroup gm) {
		GroupMembership verified = getVerifiedFbGroup(currentUser, gm);
		Pool newpool = new Pool(verified);

		List<Pool> existing = poolRepo.findByFacebookId(newpool.getFacebookId());

		if (existing.isEmpty()){
			Pool savedPool = poolRepo.save(newpool);
			return membershipRepo.save(new Membership(currentUser, savedPool, true));
			
		} else {
			Pool existingpool = existing.get(0);
			if (!isUserMemberOfPool(currentUser, existingpool)){
				return membershipRepo.save(new Membership(currentUser, existingpool, true));
			} else {
				QMembership membership = QMembership.membership;
				JPAQuery query = new JPAQuery(em);
				query.from(membership).where(
						membership.pool.eq(existingpool).and(membership.member.eq(currentUser).and(membership.approved.isTrue())));
				return query.list(membership).get(0);
			}
		}
	}

	private GroupMembership getVerifiedFbGroup(Person user, FacebookGroup gm){
		List<GroupMembership> usermemberships = getFacebookGroups(user);
		for (GroupMembership m: usermemberships){
			if (m.getId().equalsIgnoreCase(gm.getId())){
				return m;
			}
		}
		throw new TjingException("User is not a legit member of that group");
	}

	public List<Membership> getPoolMemberships(Person currentUser, Integer poolId) {
		Pool pool = poolRepo.findOne(poolId);
		
		if (!isUserMemberOfPool(currentUser, pool)){
			throw new TjingException("Only members may access this");
		} else {
			JPAQuery query = new JPAQuery(em);
			QMembership membership = QMembership.membership;
			query.from(membership)
			.where(membership.pool.eq(pool).and(membership.approved.isTrue()));
			
			return query.list(membership);
		}		
	}
}
