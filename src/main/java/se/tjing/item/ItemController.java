package se.tjing.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.interaction.Interaction;
import se.tjing.share.Share;
import se.tjing.user.Person;
import se.tjing.user.PersonService;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	ItemService itemService;

	@Autowired
	PersonService personService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Item> addItem(@RequestBody AddItemRestObject addItem) {
		Person currentUser = personService.getCurrentUser();

		Item addedItem = itemService.addItem(addItem
				.buildItemWithOwner(currentUser));
		return new ResponseEntity<Item>(addedItem, null, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{itemId}/share/{poolId}", method = RequestMethod.POST)
	public ResponseEntity<Share> shareItemToGroup(@PathVariable Integer itemId,
			@PathVariable Integer poolId) {
		return new ResponseEntity<Share>(itemService.shareToGroup(itemId,
				poolId), null, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/availableitems", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> getAvailableItems() {
		return new ResponseEntity<List<Item>>(
				itemService.getAvailableItemsToUser(personService
						.getCurrentUser()), null, HttpStatus.OK);
	}

	@RequestMapping(value = "/{itemId}/request", method = RequestMethod.POST)
	public ResponseEntity<Interaction> requestItem(@PathVariable Integer itemId) {
		Person currentUser = personService.getCurrentUser();
		Interaction newInteraction = itemService.initiateRequest(currentUser,
				itemId);
		return new ResponseEntity<Interaction>(newInteraction, null,
				HttpStatus.CREATED);
	}
}
