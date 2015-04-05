package se.tjing.interaction;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import se.tjing.common.TjingEntity;
import se.tjing.feed.NotificationInteraction;
import se.tjing.item.Item;
import se.tjing.rating.Rating;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Interaction extends TjingEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JsonIgnoreProperties("activeInteraction")
	private Item item;

	@ManyToOne
	private Person borrower;
	
	@OneToMany(mappedBy="event")
	private List<NotificationInteraction> notifications;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusRequested;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusAccepted;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusHandedOver;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusReturned;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusCancelled;
	
	@OneToOne
	private Rating borrowerRating;
	
	public Rating getBorrowerRating() {
		return borrowerRating;
	}

	public void setBorrowerRating(Rating borrowerRating) {
		this.borrowerRating = borrowerRating;
	}

	public Rating getOwnerRating() {
		return ownerRating;
	}

	public void setOwnerRating(Rating ownerRating) {
		this.ownerRating = ownerRating;
	}

	@OneToOne
	private Rating ownerRating;

	private Boolean active = true;

	public Interaction(Person currentUser, Item item2, DateTime now) {
		this.borrower = currentUser;
		this.item = item2;
		this.statusRequested = now;
	}
	
	public Interaction(){
		
	}

	public Boolean getActive() {
		return active;
	}

	public Person getBorrower() {
		return borrower;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Item getItem() {
		return item;
	}


	public DateTime getStatusAccepted() {
		return statusAccepted;
	}

	public DateTime getStatusCancelled() {
		return statusCancelled;
	}

	public DateTime getStatusHandedOver() {
		return statusHandedOver;
	}

	public DateTime getStatusRequested() {
		return statusRequested;
	}

	public DateTime getStatusReturned() {
		return statusReturned;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setBorrower(Person borrower) {
		this.borrower = borrower;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setItem(Item item) {
		this.item = item;
	}	

	public void setStatusAccepted(DateTime statusAccepted) {
		this.statusAccepted = statusAccepted;
	}

	public void setStatusCancelled(DateTime statusCancelled) {
		this.statusCancelled = statusCancelled;
	}
	
	public void setStatusHandedOver(DateTime statusHandedOver) {
		this.statusHandedOver = statusHandedOver;
	}
	public void setStatusRequested(DateTime statusRequested) {
		this.statusRequested = statusRequested;
	} 

	public void setStatusReturned(DateTime statusReturned) {
		this.statusReturned = statusReturned;
	}

}
