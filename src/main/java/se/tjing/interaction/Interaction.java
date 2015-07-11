package se.tjing.interaction;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import se.tjing.common.TjingEntity;
import se.tjing.feed.Notification;
import se.tjing.interactionmessage.InteractionMessage;
import se.tjing.item.Item;
import se.tjing.rating.Rating;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Interaction extends TjingEntity{

	@ManyToOne
	@JsonIgnoreProperties("connection")
	private Person borrower;
	
	private Boolean borrowerHidden = false;
	private Boolean ownerHidden = false;
	
	@JsonIgnore
	private  Boolean deleted = false;

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@OneToOne
	private Rating borrowerRating;

	@OneToMany(mappedBy="interaction", cascade=CascadeType.ALL)
	@OrderBy("creation_time ASC")
	private List<InteractionMessage> conversation;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JsonIgnoreProperties("activeInteraction")
	private Item item;
	
	@OneToMany(mappedBy="interaction", cascade=CascadeType.ALL)
	private List<Notification> notifications;

	@OneToOne
	private Rating ownerRating;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusAccepted;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusCancelled;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusHandedOver;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusRequested;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusReturned;
	
	public Interaction(){
		
	}

	public Interaction(Person currentUser, Item item2, DateTime now) {
		this.borrower = currentUser;
		this.item = item2;
		this.statusRequested = now;
	}
	
	public boolean getActive(){
		if (this.statusAccepted == null || this.statusRequested!=null) return false;
		else return true;
	}

	public Person getBorrower() {
		return borrower;
	}

	public Rating getBorrowerRating() {
		return borrowerRating;
	}

	public List<InteractionMessage> getConversation() {
		return conversation;
	}

	@Override
	public Integer getId() {
		return id;
	}
	
	public Item getItem() {
		return item;
	}

	public Rating getOwnerRating() {
		return ownerRating;
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

	public void setBorrower(Person borrower) {
		this.borrower = borrower;
	}

	public void setBorrowerRating(Rating borrowerRating) {
		this.borrowerRating = borrowerRating;
	}

	public void setConversation(List<InteractionMessage> conversation) {
		this.conversation = conversation;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setOwnerRating(Rating ownerRating) {
		this.ownerRating = ownerRating;
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

	public Boolean getBorrowerHidden() {
		return borrowerHidden;
	}

	public void setBorrowerHidden(Boolean borrowerHidden) {
		this.borrowerHidden = borrowerHidden;
	}

	public Boolean getOwnerHidden() {
		return ownerHidden;
	}

	public void setOwnerHidden(Boolean ownerHidden) {
		this.ownerHidden = ownerHidden;
	}

}
