package se.tjing.user;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import se.tjing.common.BaseEntity;
import se.tjing.interaction.Interaction;
import se.tjing.item.Item;
import se.tjing.membership.Membership;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Person extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	// TODO sort out the fullname business. Plenty of opportunity for stuff to
	// go wrong here. Sorry
	private String firstName;
	private String lastName;
	private String fullName;
	private String username; // probably going to be email
	private String facebookId;

	public Set<Interaction> getInteractions() {
		return interactions;
	}

	public void setInteractions(Set<Interaction> interactions) {
		this.interactions = interactions;
	}

	@JsonIgnore
	private String password;

	@OneToMany(mappedBy = "owner")
	@JsonManagedReference
	private Set<Item> items;

	@OneToMany(mappedBy = "member")
	@JsonManagedReference
	private Set<Membership> memberships;

	@OneToMany(mappedBy = "borrower")
	@JsonBackReference
	private Set<Interaction> interactions;

	public Person() {
		super();
	}

	public Person(String email, String password, String firstName,
			String lastName) {
		this.username = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.setPassword(password);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		this.fullName = firstName + " " + lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		this.fullName = firstName + " " + lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String email) {
		this.username = email;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Membership> getMemberships() {
		return memberships;
	}

	public void setMemberships(Set<Membership> memberships) {
		this.memberships = memberships;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
