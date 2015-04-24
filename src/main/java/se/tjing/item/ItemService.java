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

	public Item addItem(Item item) {
		// TODO: Business logic. Check for existing items
		itemRepo.save(item);
		return item;
	}

	public List<Item> getAvailableItemsToUser(Person user) {
		QItem item = QItem.item;
		QShare share = QShare.share;
		QPool pool = QPool.pool;
		QMembership membership = QMembership.membership;
		
		//"Get all items shared with groups I am a member of and are not owned by me"
		JPAQuery query = new JPAQuery(em).from(item)
				.leftJoin(item.shares, share).leftJoin(share.pool, pool)
				.leftJoin(pool.memberships, membership)
				.where(itemIsAvailableViaPools(user));
		List<Item> result = query.distinct().list(item);

		//Get facebook shared items
		List<Person> fbFriends = personService.getUsersFacebookFriends(user);
		JPAQuery fbquery = new JPAQuery(em);
		fbquery.from(item).where(item.owner.in(fbFriends).and(item.fbAvailable.isTrue()));
		
		result.addAll(fbquery.list(item));

		return result;
	}

	public Item getOtherUsersItem(Person person, Integer itemId) {
		Item result = itemRepo.findOne(itemId);
		if (result == null || !isItemAvailableToUser(person, result)) {
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
	
	public Boolean isItemAvailableToUser(Person user, Item item) {
		if (item.getOwner().equals(user)) {
			throw new TjingException("It's your own thing, dummy!");
		}
		if (item.getFbAvailable()){
			return personService.areFacebookFriends(user, item.getOwner());
		}
		QShare share = QShare.share;
		QPool pool = QPool.pool;
		QItem itemtable = QItem.item;
		
		QMembership membership = QMembership.membership;
		
		// "Is there a group in which the user is a member to which the item is shared?"
		JPAQuery query = new JPAQuery(em).from(share)
				.leftJoin(share.pool, pool)
				.leftJoin(pool.memberships, membership)
				.leftJoin(share.item, itemtable)
				.where(itemIsAvailableViaPools(user));
		return query.exists();
	}

	private BooleanExpression itemIsAvailableViaPools(Person user){
		QMembership membership = QMembership.membership;
		QItem item = QItem.item;
		
		return membership.member.eq(user).and(membership.approved.isTrue()).and(item.owner.ne(user));
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
		.where(itemIsAvailableViaPools(person)
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
}
