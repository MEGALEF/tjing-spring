package se.tjing.itemrequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.user.PersonService;

@RestController
@RequestMapping("itemrequest")
public class ItemRequestController {
	@Autowired
	PersonService personService;
	
	@Autowired
	ItemRequestService itemreqService;
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<ItemRequest> postRequest(@RequestBody AddItemRequest request){
		ItemRequest result = itemreqService.postRequest(personService.getCurrentUser(), request);
		return new ResponseEntity<ItemRequest>(result, null, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ItemRequest>> getRequests(){
		List<ItemRequest> result = itemreqService.getUserRequests(personService.getCurrentUser());
		
		return new ResponseEntity<List<ItemRequest>>(result, null, HttpStatus.OK);
	}
}
