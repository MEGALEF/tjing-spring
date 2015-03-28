package se.tjing.item;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.interaction.Interaction;
import se.tjing.interaction.InteractionRepository;
import se.tjing.interaction.QInteraction;
import se.tjing.membership.QMembership;
import se.tjing.pool.Pool;
import se.tjing.pool.PoolRepository;
import se.tjing.pool.QPool;
import se.tjing.share.QShare;
import se.tjing.share.Share;
import se.tjing.share.ShareRepository;
import se.tjing.user.Person;

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

	@Autowired
	InteractionRepository interactionRepo;

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

	public Item getItem(Person person, Integer itemId) {
		Item result = itemRepo.findOne(itemId);
		if (result == null || !isItemAvailableToUser(person, result)) {
			throw new TjingException("Item does not exist or is not available");
		} else {
			return result;
		}

	}

	private Boolean isItemAvailableToUser(Person person, Item item) {
		if (item.getOwner().equals(person)) {
			return true;
		}
		QShare share = QShare.share;
		QPool pool = QPool.pool;
		QMembership membership = QMembership.membership;
		// "Is there a group in which the user is a member to which the item is shared?"
		JPAQuery query = new JPAQuery(em).from(share)
				.leftJoin(share.pool, pool)
				.leftJoin(pool.memberships, membership)
				.where(membership.member.eq(person).and(share.item.eq(item)));
		return query.exists();
	}

	public List<Item> getAvailableItemsToUser(Person p) {
		QItem item = QItem.item;
		QShare share = QShare.share;
		QPool pool = QPool.pool;
		QMembership membership = QMembership.membership;
		JPAQuery query = new JPAQuery(em).from(item)
				.leftJoin(item.shares, share).leftJoin(share.pool, pool)
				.leftJoin(pool.memberships, membership)
				.where(membership.member.eq(p));

		return query.list(item);
	}

	public List<Item> searchAvailableItems(Person person, String searchStr) {
		QItem item = QItem.item;
		QShare share = QShare.share;
		QPool pool = QPool.pool;
		QMembership membership = QMembership.membership;
		JPAQuery query = new JPAQuery(em)
				.from(item)
				.leftJoin(item.shares, share)
				.leftJoin(share.pool, pool)
				.leftJoin(pool.memberships, membership)
				.where(membership.member.eq(person).and(
						item.title.containsIgnoreCase(searchStr)));

		return query.list(item);
	}

	// TODO: Move to InteractionService?
	public Interaction initiateRequest(Person currentUser, Integer itemId) {
		Item item = itemRepo.findOne(itemId);
		if (item == null || !isItemAvailableToUser(currentUser, item)) {
			throw new TjingException(
					"Item does not exist or is not available to you");
		}
		Interaction interaction = new Interaction();
		interaction.setBorrower(currentUser);
		interaction.setItem(item);
		interaction.setStatusRequested(new DateTime());
		return interactionRepo.save(interaction);
	}

	public List<Item> getUsersItems(Person user) {
		QItem item = QItem.item;
		JPAQuery query = new JPAQuery(em);
		query.from(item).where(item.owner.eq(user));
		return query.list(item);
	}

	public List<Item> getUsersBorrowedItems(Person person) {
		QInteraction interaction = QInteraction.interaction;
		QItem item = QItem.item;
		JPAQuery query = new JPAQuery(em);
		query.from(item)
				.leftJoin(item.interactions, interaction)
				.where(interaction.borrower.eq(person)
						.and(interaction.statusHandedOver.isNotNull())
						.and(interaction.statusReturned.isNull()));
		return query.list(item);
	}

	public void removeItem(Person user, Integer itemId) {
		Item item = itemRepo.findOne(itemId);
		if (!item.getOwner().equals(user)){
			throw new TjingException("Only the item owner may delete it");
		} else if (item.getActiveInteraction() != null){
			throw new TjingException("This item is in active transaction"); //TODO: PRobabla stuff to do here
		} else {
			shareRepo.delete(item.getShares());
			interactionRepo.delete(item.getInteractions());
			itemRepo.delete(item);
		}
	}

	public List<Share> unshareItemFromPool(Person currentUser, Integer itemId,
			Integer poolId) {
		Item item = itemRepo.findOne(itemId);
		Pool pool = poolRepo.findOne(poolId);
		
		if (!item.getOwner().equals(currentUser)){
			throw new TjingException("Only the item owner may do this");
		} else {
			JPAQuery query = new JPAQuery(em);
			QShare share = QShare.share;
			query.from(share).where(share.item.eq(item).and(share.pool.eq(pool)));
			List<Share> queryresult = query.list(share);
			for (Share res : queryresult){
				shareRepo.delete(res);
			}
			
			return item.getShares();
		}
	}

}
