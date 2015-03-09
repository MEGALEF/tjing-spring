package se.tjing.item;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import se.tjing.common.BaseEntity;
import se.tjing.interaction.Interaction;
import se.tjing.share.Share;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class Item extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String description;

	private String title;

	@ManyToOne
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private Person owner;

	@OneToMany(mappedBy = "item")
	@JsonIgnore
	private List<Share> shares;

	@OneToMany(mappedBy = "item")
	@JsonIgnore
	private Set<Interaction> interactions;

	@OneToOne
	private Interaction activeInteraction;

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

	public Interaction getActiveInteraction() {
		return activeInteraction;
	}

	public void setActiveInteraction(Interaction activeInteraction) {
		this.activeInteraction = activeInteraction;
	}

}
