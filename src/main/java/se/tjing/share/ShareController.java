package se.tjing.share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.user.PersonService;

@RestController
@RequestMapping("/share")
public class ShareController {
	
	@Autowired
	PersonService personService;
	
	@Autowired
	ShareService shareService;

	@RequestMapping(value="{shareId}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteShare(@PathVariable Integer shareId){
		shareService.removeShare(personService.getCurrentUser(), shareId);
		return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Share> addShare(@RequestBody NewShare newShare){
		Share addedShare = shareService.shareToGroup(personService.getCurrentUser(), newShare);
		return new ResponseEntity<Share>(addedShare, null, HttpStatus.CREATED);
	}
}
