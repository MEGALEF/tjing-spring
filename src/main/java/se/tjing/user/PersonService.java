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
		if (personRepo.findByEmail(person.getEmail()) != null) {
			throw new TjingException("User with that email already exists");
		}
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
		return personRepo.findByEmail(email);
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
		person.setEmail(username);
		personRepo.save(person);
	}

}
