package se.tjing.item;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import se.tjing.common.BaseEntity;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Item extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String description;

	private String title;

	@ManyToOne
	@JsonBackReference
	private Person owner;

	public Item() {
	}

	public Item(Person owner, String description, String title) {
		this.owner = owner;
		this.description = description;
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public Person getOwner() {
		return owner;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
