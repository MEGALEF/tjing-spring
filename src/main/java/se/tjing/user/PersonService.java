package se.tjing.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepo;

	public Person addPerson(Person person) {
		if (personRepo.findByUsername(person.getUsername()) != null) {
			throw new TjingException("User with that email already exists");
		}
		// if (personRepo.findByFacebookId(person.getFacebookId()) != null) {
		// throw new TjingException(
		// "A user connected to that facebook account already exists");
		// }
		personRepo.save(person);
		return person;
	}

	public Person getPerson(Integer userId) {
		Person result = personRepo.findOne(userId);
		if (result == null) {
			throw new TjingException("User with that id does not exist");
		} else {
			return result;
		}
	}

	public Person getPersonByEmail(String email) {
		return personRepo.findByUsername(email);
	}

	public Person getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		User currentUserObject = (User) authentication.getPrincipal();
		Person currentUser = getPersonByEmail(currentUserObject.getUsername());
		return currentUser;
	}

	public void addPerson(String username) {
		Person person = new Person();
		person.setUsername(username);
		personRepo.save(person);
	}

}
