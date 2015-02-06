package se.tjing.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

	@Autowired
	ItemRepository itemRepo;

	public Item addItem(Item item) {
		// TODO: Business logic
		itemRepo.save(item);
		return item;
	}

}
