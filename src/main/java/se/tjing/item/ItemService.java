package se.tjing.item;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.membership.QMembership;
import se.tjing.pool.Pool;
import se.tjing.pool.PoolRepository;
import se.tjing.pool.QPool;
import se.tjing.share.QShare;
import se.tjing.share.Share;
import se.tjing.share.ShareRepository;
import se.tjing.user.Person;
import se.tjing.user.QPerson;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class ItemService {

	@Autowired
	EntityManager em;

	@Autowired
	ItemRepository itemRepo;

	@Autowired
	PoolRepository poolRepo;

	@Autowired
	ShareRepository shareRepo;

	public Item addItem(Item item) {
		// TODO: Business logic. Check for existing items
		itemRepo.save(item);
		return item;
	}

	public Share shareToGroup(Integer itemId, Integer poolId) {
		Pool pool = poolRepo.findOne(poolId);
		Item item = itemRepo.findOne(itemId);
		// TODO: Business logic. Look for already existing shares
		Share share = new Share(item, pool);
		return shareRepo.save(share);
	}

	public Boolean isItemAvailableToUser(Person person, Item item) {
		// TODO
		return false;
	}

	public List<Item> getAvailableItemsToUser(Person p) {
		QItem item = QItem.item;
		QShare share = QShare.share;
		QPool pool = QPool.pool;
		QMembership membership = QMembership.membership;
		QPerson person = QPerson.person;
		JPAQuery query = new JPAQuery(em).from(item)
				.leftJoin(item.shares, share).leftJoin(share.pool, pool)
				.leftJoin(pool.memberships, membership)
				.where(membership.member.eq(p));

		return query.list(item);
	}

	public List<Item> searchAvailableItems(Person person, String searchStr) {
		// TODO
		return null;
	}

}
