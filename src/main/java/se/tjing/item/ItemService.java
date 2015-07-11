package se.tjing.item;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.image.ItemPicture;
import se.tjing.image.PictureRepository;
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
import se.tjing.user.PersonService;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;

@Service
public class ItemService {

	@Autowired
	EntityManager em;

	@Autowired
	ItemRepository itemRepo;

	@Autowired
	PictureRepository picRepo;

	@Autowired
	PoolRepository poolRepo;

	@Autowired
	ShareRepository shareRepo;

	@Autowired
	InteractionRepository interactionRepo;

	@Autowired
	PersonService personService;
	
	@Autowired
	ItemCategoryService catService;

	public Item addItem(Person user, Item item) {
		if (item.getCategory()!=null){
			ItemCategory cat = catService.findOrAdd(item.getCategory().getName());
			item.setCategory(cat);
		}
		if(item.getOwner() == null){
			item.setOwner(user);
		}
		
		return itemRepo.save(item);
	}

	public List<Item> getAvailableItems(Person user, Integer limit) {
		QItem item = QItem.item;
		QShare share = QShare.share;
		QPool pool = QPool.pool;
		QMembership membership = QMembership.membership;
		
		//"Get all items shared with groups I am a member of"
		JPAQuery query = new JPAQuery(em).from(item)
				.leftJoin(item.shares, share)
				.leftJoin(share.pool, pool)
				.leftJoin(pool.memberships, membership)
				.where(itemIsAvailableToUser(user)).orderBy(item.addedTime.desc()).limit(limit);
		List<Item> result = query.distinct().list(item);

		//Get facebook shared items
//		List<Person> fbFriends = personService.getUsersFacebookFriends(user);
//		JPAQuery fbquery = new JPAQuery(em);
//		fbquery.from(item)
//		.where(item.owner.in(fbFriends).and(item.fbAvailable.isTrue()))
//		.limit(limit);

//		result.addAll(fbquery.list(item));

		return result;
	}

	public Item getItem(Person person, Integer itemId) {
		Item result = itemRepo.findOne(itemId);
		if (result == null || !isItemVisibleToUser(person, result)) {
			throw new TjingException("Item does not exist or is not available");
		} else {
			return result;
		}
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

	public List<Item> getUsersItems(Person user) {
		QItem item = QItem.item;
		JPAQuery query = new JPAQuery(em);
		query.from(item).where(item.owner.eq(user));
		return query.list(item);
	}
	
	public Boolean isItemVisibleToUser(Person user, Item item) {
		if (item.isSharedPublic()) return true;
		if (item.getOwner().equals(user)) {
			return true;
		}
		if (item.getFbAvailable() && personService.areFacebookFriends(user, item.getOwner())){
			return true;
		}
		
		QShare share = QShare.share;
		QPool pool = QPool.pool;
		QItem itemtable = QItem.item;
		QMembership membership = QMembership.membership;
		
		// "Is there a group in which the user is a member to which the item is shared?"
		JPAQuery query = new JPAQuery(em).from(itemtable)
				.leftJoin(itemtable.shares, share)
				.leftJoin(share.pool, pool)
				.leftJoin(pool.memberships, membership)
				.where(itemIsAvailableToUser(user));
		return query.exists();
	}

	private BooleanExpression itemIsAvailableToUserExcludingUser(Person user){
		QItem item = QItem.item;
		
		return itemIsAvailableToUser(user)
				.and(item.owner.ne(user));
	}
	
	private BooleanExpression itemIsAvailableToUser(Person user){
		QMembership membership = QMembership.membership;
		QItem item = QItem.item;
		
		return membership.member.eq(user).and(membership.approved.isTrue())
				.or(item.sharedPublic.isTrue());
	}

	public void removeItem(Person user, Integer itemId) {
		Item item = itemRepo.findOne(itemId);
		if (!item.getOwner().equals(user)){
			throw new TjingException("Only the item owner may delete it");
		} else if (item.getActiveInteraction() != null){
			throw new TjingException("This item is in active transaction");
		} else {
			shareRepo.delete(item.getShares());
			interactionRepo.delete(item.getInteractions());
			itemRepo.delete(item);
		}
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
		.where(itemIsAvailableToUserExcludingUser(person).or(item.sharedPublic.isTrue())
				.and(item.title.containsIgnoreCase(searchStr)));
		//TODO add facebook items

		return query.list(item);
	}

	public Item setFbAvailable(Person currentUser, PartialFbAvailable partial) {
		Item item = itemRepo.findOne(partial.getId());
		if (item==null || !item.getOwner().equals(currentUser)){
			throw new TjingException("Only the item owner is allowed to do this");
		} else {
			item.setFbAvailable(partial.getFbAvailable());
			return itemRepo.save(item);
		}
	}

	public Item updateItem(Person currentUser, Integer itemId, Item update) {
		Item item = itemRepo.findOne(itemId);
		if (item==null) throw new TjingException("No such item");
		if (update.isSharedPublic() != null) item.setSharedPublic(update.isSharedPublic());
		if (update.getFbAvailable() != null) item.setFbAvailable(update.getFbAvailable());
		
		return itemRepo.save(item);
	}

	public List<Item> getOtherUsersItems(Person currentUser, Integer ownerId) {
		Person otherUser = personService.getPerson(ownerId);
		QItem item = QItem.item;
		QShare share = QShare.share;
		QPool pool = QPool.pool;
		QMembership membership = QMembership.membership;
		
		JPAQuery query = new JPAQuery(em).from(item)
				.leftJoin(item.shares, share)
				.leftJoin(share.pool, pool)
				.leftJoin(pool.memberships, membership)
				.where(itemIsAvailableToUserExcludingUser(currentUser).and(item.owner.eq(otherUser)));
		
		return query.list(item);
	}
}
