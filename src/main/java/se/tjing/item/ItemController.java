package se.tjing.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
		Person currentUser = getCurrentUser();

		Item addedItem = itemService.addItem(addItem
				.buildItemWithOwner(currentUser));
		return new ResponseEntity<Item>(addedItem, null, HttpStatus.CREATED);
	}

	public Person getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		String currentUserName = (String) authentication.getPrincipal();
		Person currentUser = personService.getPersonByEmail(currentUserName);
		return currentUser;
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
				itemService.getAvailableItemsToUser(getCurrentUser()), null,
				HttpStatus.OK);
	}
}
