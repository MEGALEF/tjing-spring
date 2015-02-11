package se.tjing.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.pool.Pool;
import se.tjing.pool.PoolRepository;
import se.tjing.share.Share;
import se.tjing.share.ShareRepository;
import se.tjing.user.Person;

@Service
public class ItemService {

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

	public List<Item> getAvailableItemsToUser(Person person) {
		// TODO
		return null;
	}

	public List<Item> searchAvailableItems(Person person, String searchStr) {
		// TODO
		return null;
	}

}
