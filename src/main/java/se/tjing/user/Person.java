package se.tjing.user;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import se.tjing.common.BaseEntity;
import se.tjing.item.Item;
import se.tjing.membership.Membership;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Person extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String firstName;
	private String lastName;
	private String email;
	private String password;

	@OneToMany(mappedBy = "owner")
	@JsonManagedReference
	private Set<Item> items;

	@OneToMany(mappedBy = "member")
	@JsonManagedReference
	private Set<Membership> memberships;

	public Person() {

	}

	public Person(String email, String password, String firstName,
			String lastName) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.setPassword(password);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
}
