package se.tjing.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.social.connect.UsersConnectionRepository;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.interaction.Interaction;
import se.tjing.item.Item;
import se.tjing.membership.Membership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysema.query.jpa.impl.JPAQuery;

public class PostPerson {

	private String firstName;
	
	private String description;
	private String locationStr;



	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String lastName;

	private String username;
	

	public PostPerson() {
		//super();
	}


	public String getFirstName() {
		return firstName;
	}


	public Integer getId() {
		return id;
	}



	public String getLastName() {
		return lastName;
	}

	
	public String getUsername() {
		return username;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public void setUsername(String email) {
		this.username = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocationStr() {
		return locationStr;
	}

	public void setLocationStr(String locationStr) {
		this.locationStr = locationStr;
	}
}
