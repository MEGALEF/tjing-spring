package se.tjing.item;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import se.tjing.exception.TjingException;
import se.tjing.image.ImageService;
import se.tjing.image.ItemPicture;
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
	
	@Autowired
	ImageService imageService;
	
	@RequestMapping(value="{itemId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteItem(@PathVariable Integer itemId){
		itemService.removeItem(personService.getCurrentUser(), itemId);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Item> createItem(@RequestBody Item newItem) {
		Person currentUser = personService.getCurrentUser();
		Item addedItem = itemService.addItem(currentUser, newItem);
		return new ResponseEntity<Item>(addedItem, null, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="{itemId}", method = RequestMethod.PATCH)
	@ResponseBody
	public ResponseEntity<Item> updateItem(@RequestBody Item update, @PathVariable Integer itemId){
		Item result = itemService.updateItem(personService.getCurrentUser(), itemId, update);
		
		return new ResponseEntity<Item>(result, null, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Item>> getItems(
			@RequestParam(value = "param", required=false) String param, 
			@RequestParam(value="owner", required=false) Integer ownerId, 
			@RequestParam(value="limit", defaultValue="20") Integer limit) {
		List<Item> result;
		Person currentUser = personService.getCurrentUser();
		if(ownerId != null){
			result = itemService.getOtherUsersItems(currentUser, ownerId);
		} //TODO: Make this consistent. Will require frontend changes
		else {
			if ("owned".equals(param)){
				result = itemService.getUsersItems(currentUser);
			} else {
				result = itemService.getAvailableItems(currentUser, limit);
			}
		}
		
		return new ResponseEntity<List<Item>>(
				result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<Item> getItem(@PathVariable Integer itemId) {
		Person currentUser = personService.getCurrentUser();
		return new ResponseEntity<Item>(
				itemService.getItem(currentUser, itemId), null, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{itemId}/fbavailable", method=RequestMethod.POST)
	public ResponseEntity<Item> setFbAvailable(@RequestBody PartialFbAvailable fbAvailable, @PathVariable Integer itemId){
		Item result = itemService.setFbAvailable(personService.getCurrentUser(), fbAvailable);
		return new ResponseEntity<Item>(result, null, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/search/{searchString}", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> searchItems(@PathVariable String searchString) {
		List<Item> result = itemService.searchAvailableItems(
				personService.getCurrentUser(), searchString);
		return new ResponseEntity<List<Item>>(result, null, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{itemId}/image" ,method=RequestMethod.POST)
	public ResponseEntity<ItemPicture> handleFileUpload(@PathVariable Integer itemId, @RequestParam("file") MultipartFile file){
		Integer MAX_SIZE = 512000;
		if (file.isEmpty() || file.getSize()>MAX_SIZE){
			throw new TjingException("Image error");
		} else {
			byte[] bytes;
			try {
				bytes = file.getBytes();
			} catch (IOException e) {
				throw new TjingException(e.getMessage());
			}
			ItemPicture image = imageService.saveImage(itemId, bytes, personService.getCurrentUser());
			return new ResponseEntity<ItemPicture>(image, null, HttpStatus.CREATED);
		}
		
	}
}
