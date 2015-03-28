package se.tjing.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(value = "/borrowed", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> getBorrowedItems() {
		List<Item> result = itemService.getUsersBorrowedItems(personService
				.getCurrentUser());
		return new ResponseEntity<List<Item>>(result, null, HttpStatus.OK);
	}
	
	@RequestMapping(value="{itemId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteItem(@PathVariable Integer itemId){
		itemService.removeItem(personService.getCurrentUser(), itemId);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/owned", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> getOwnItems() {
		Person currentUser = personService.getCurrentUser();
		List<Item> result = itemService.getUsersItems(currentUser);
		return new ResponseEntity<List<Item>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Item> addItem(@RequestBody AddItemRestObject addItem) {
		Person currentUser = personService.getCurrentUser();
		Item addedItem = itemService.addItem(addItem
				.buildItemWithOwner(currentUser));
		return new ResponseEntity<Item>(addedItem, null, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Item>> getItems(@RequestParam(value = "param", required=false) String param) {
		if ("owned".equals(param)){
			return this.getOwnItems();
		} else if ("borrowed".equals(param)){
			return this.getBorrowedItems();
		}
		return new ResponseEntity<List<Item>>(
				itemService.getAvailableItemsToUser(personService
						.getCurrentUser()), null, HttpStatus.OK);
	}

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<Item> getItem(@PathVariable Integer itemId) {
		Person currentUser = personService.getCurrentUser();
		return new ResponseEntity<Item>(
				itemService.getItem(currentUser, itemId), null, HttpStatus.OK);
	}

	@RequestMapping(value = "/{itemId}/sharetopool/{poolId}", method = RequestMethod.POST)
	public ResponseEntity<Share> shareItemToGroup(@PathVariable Integer itemId,
			@PathVariable Integer poolId) {
		return new ResponseEntity<Share>(itemService.shareToGroup(itemId,
				poolId), null, HttpStatus.CREATED); //TODO: Only item owner should be allowed to do this
	}
	
	@RequestMapping(value="/{itemId}/unsharefrompool/{poolId}", method = RequestMethod.DELETE)
	public ResponseEntity<List<Share>> unshareItemFromPool(@PathVariable Integer itemId, @PathVariable Integer poolId){
		List<Share> result = itemService.unshareItemFromPool(personService.getCurrentUser(), itemId, poolId);
		return new ResponseEntity<List<Share>>(result, null, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/{itemId}/request", method = RequestMethod.POST)
	public ResponseEntity<Interaction> requestItem(@PathVariable Integer itemId) {
		Person currentUser = personService.getCurrentUser();
		Interaction newInteraction = itemService.initiateRequest(currentUser,
				itemId);
		return new ResponseEntity<Interaction>(newInteraction, null,
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/search/{searchString}", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> search(@PathVariable String searchString) {
		List<Item> result = itemService.searchAvailableItems(
				personService.getCurrentUser(), searchString);
		return new ResponseEntity<List<Item>>(result, null, HttpStatus.OK);
	}
}
