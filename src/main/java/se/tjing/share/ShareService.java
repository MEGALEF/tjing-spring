package se.tjing.share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.user.Person;

@Service
public class ShareService {
	
	@Autowired
	ShareRepository shareRepo;
	
	public void remove(Person user, Integer shareId){
		Share share = shareRepo.findOne(shareId);
		if (!share.getItem().getOwner().equals(user)){
			throw new TjingException("Only the item owner may do this");
		} else {
			shareRepo.delete(share);
		}
	}
}
