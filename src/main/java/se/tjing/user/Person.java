package se.tjing.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.UsersConnectionRepository;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.interaction.Interaction;
import se.tjing.item.Item;
import se.tjing.membership.Membership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysema.query.jpa.impl.JPAQuery;

@Entity
public class Person extends TjingEntity implements Serializable {
	

	// TODO sort out the fullname business. Plenty of opportunity for stuff to
	// go wrong here. Sorry
	private String firstName;
	private String fullName;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(mappedBy = "borrower")
	@JsonIgnore
	private Set<Interaction> interactions;

	@OneToMany(mappedBy = "owner")
	@JsonIgnore
	private Set<Item> items;

	private String lastName;

	@OneToMany(mappedBy = "member")
	@JsonIgnore
	private List<Membership> memberships;

	@JsonIgnore
	private String password;

	@JsonIgnore
	private String username;
	
	@OneToMany(mappedBy="person")
	private List<UserConnection> connection;

	public List<UserConnection> getConnection() {
		return connection;
	}

	public void setConnection(List<UserConnection> connection) {
		this.connection = connection;
	}

	public Person() {
		super();
	}

	public Person(String email, String password, String firstName,
			String lastName) {
		this.username = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password=password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getFullName() {
		return fullName;
	}

	public Integer getId() {
		return id;
	}

	public Set<Interaction> getInteractions() {
		return interactions;
	}

	public Set<Item> getItems() {
		return items;
	}

	public String getLastName() {
		return lastName;
	}

	public List<Membership> getMemberships() {
		return memberships;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		this.fullName = firstName + " " + lastName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInteractions(Set<Interaction> interactions) {
		this.interactions = interactions;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		this.fullName = firstName + " " + lastName;
	}

	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String email) {
		this.username = email;
	}
}
