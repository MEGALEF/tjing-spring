package se.tjing.config;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

import se.tjing.user.PersonService;

public class AccountConnectionSignUpService implements ConnectionSignUp {

	private final PersonService personService;

	public AccountConnectionSignUpService(PersonService personService) {
		this.personService = personService;
	}

	//TODO: Right now the application does not support implicit signups. 
	@Override
	public String execute(Connection<?> connection) {
		UserProfile profile = connection.fetchUserProfile();
		return profile.getUsername();
	}

}
