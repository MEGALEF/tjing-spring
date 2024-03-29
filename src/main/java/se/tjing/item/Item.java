package se.tjing.item;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import se.tjing.common.TjingEntity;
import se.tjing.image.ItemPicture;
import se.tjing.interaction.Interaction;
import se.tjing.share.Share;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Item extends TjingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String description;
	
	private DateTime addedTime;
	
	@OneToOne
	private ItemCategory category;
	
	private Boolean sharedPublic;

	private String title;
	
	@OneToMany(mappedBy="item", cascade=CascadeType.ALL)
	private List<ItemPicture> images;

	public List<ItemPicture> getImages() {
		return images;
	}

	public void setImages(List<ItemPicture> images) {
		this.images = images;
	}

	@ManyToOne
//	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//	@JsonIdentityReference(alwaysAsId = true)
	private Person owner;

	@OneToMany(mappedBy = "item", cascade=CascadeType.ALL)
	@JsonIgnoreProperties("item")
	private List<Share> shares;
	
	//@JsonIgnore
	private Boolean fbAvailable;

	public List<Share> getShares() {
		return shares;
	}

	public void setShares(List<Share> shares) {
		this.shares = shares;
	}

	public Set<Interaction> getInteractions() {
		return interactions;
	}

	public void setInteractions(Set<Interaction> interactions) {
		this.interactions = interactions;
	}

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

	public Boolean getFbAvailable() {
		if (fbAvailable == null) return false;
		else return fbAvailable;
	}

	public void setFbAvailable(Boolean fbAvailable) {
		this.fbAvailable = fbAvailable;
	}
	
	public Boolean isOwnedBy(Person user){
		return this.owner.equals(user);
	}

	public Boolean isSharedPublic() {
		if (sharedPublic == null) return false;
		else return sharedPublic;
	}

	public void setSharedPublic(boolean sharedPublic) {
		this.sharedPublic = sharedPublic;
	}

	public ItemCategory getCategory() {
		return category;
	}

	public void setCategory(ItemCategory category) {
		this.category = category;
	}
	
	@PrePersist
	public void prePersist() {
		DateTime now = DateTime.now();
		this.creationTime = now;
		this.modificationTime = now;
		this.addedTime = now;
	}

}
