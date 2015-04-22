package se.tjing.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.membership.QMembership;
import se.tjing.pool.Pool;
import se.tjing.pool.QPool;

import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class PersonService {

	@Autowired
	EntityManager em;

	@Autowired
	private PersonRepository personRepo;

	@Autowired
	UserConnectionRepository userConnRepo;
	
	@Autowired
	Facebook facebook;

	public Person addPerson(Person person) {
		if (personRepo.findByUsername(person.getUsername()) != null) {
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

	public List<Person> search(String searchStr) {
		QPerson person = QPerson.person;
		JPAQuery query = new JPAQuery(em);
		query.from(person)
		.where(person.fullName.containsIgnoreCase(searchStr).or(
				person.firstName.containsIgnoreCase(searchStr).or(
						person.lastName.containsIgnoreCase(searchStr))));
		return query.list(person);
	}

	public void connectUser(String username) {
		//If there are connections for this username, hook them up to the Person object. This would be nicer with Spring Social JPA (TODO)
		Person person = personRepo.findByUsername(username);		

		QUserConnection userconn = QUserConnection.userConnection;
		JPAQuery query = new JPAQuery(em);
		query.from(userconn).where(userconn.userId.eq(username));
		if (query.exists()){
			for(UserConnection conn : query.list(userconn)){
				conn.setPerson(person);
				userConnRepo.save(conn);
			}
		}
	}

	public List<Person> getVisibleUsers(Person current) {
		QPerson person = QPerson.person;
		QMembership membership = QMembership.membership;
		QPool pool = QPool.pool;
		
		JPASubQuery poolsQ = new JPASubQuery();
		poolsQ.from(pool).leftJoin(pool.memberships, membership)
		.where(membership.member.eq(current).and(membership.approved.isTrue()));
		
		JPAQuery query = new JPAQuery(em);
		
		// Get all users visible to the current user. Not including the current user.
		query.from(person).leftJoin(person.memberships, membership)
		.where(membership.pool.in(poolsQ.list(pool)).and(membership.member.ne(current)));
		
		List<Person> result = query.distinct().list(person);
		
		return result;
	}
	
	public boolean areFacebookFriends(Person a, Person b){
		
		//This is horrible 
		//TODO: Find a better way to test if two users are facebook friends
		if (a.getConnection()==null || a.getConnection().isEmpty() || b.getConnection() == null || b.getConnection().isEmpty()){
			return false;
		}
		String aId = a.getConnection().get(0).getProviderUserId();
		String bId = b.getConnection().get(0).getProviderUserId();
		
		for (String id : facebook.friendOperations().getFriendIds(aId)){
			if (id.equals(bId)) return true;
		}
		return false;
	}

	public List<Person> getUsersFacebookFriends(Person p) {	
		List<UserConnection> connections = p.getConnection();
		
		if (connections!=null && !connections.isEmpty()){
			String userId = connections.get(0).getProviderUserId();
			List<String> friendIds = facebook.friendOperations().getFriendIds(userId);

			JPAQuery query = new JPAQuery(em);
			QPerson person = QPerson.person;
			QUserConnection connection = QUserConnection.userConnection;

			query.from(person).leftJoin(person.connection, connection)
			.where(connection.providerUserId.in(friendIds));

			return query.list(person);
		} else {
			return new ArrayList<Person>();
		}
	}
}
