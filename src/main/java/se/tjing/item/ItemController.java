package se.tjing.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		System.out.println(addItem.getTitle());
		// System.out.println(authentication.getPrincipal());
		String currentUserName = (String) authentication.getPrincipal();
		Person currentUser = personService.getPersonByEmail(currentUserName);
		System.out.println(currentUser);
		Item addedItem = itemService.addItem(addItem
				.buildItemWithOwner(currentUser));
		return new ResponseEntity<Item>(addedItem, null, HttpStatus.CREATED);
	}
}
