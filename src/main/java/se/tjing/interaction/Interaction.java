package se.tjing.interaction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import se.tjing.common.BaseEntity;
import se.tjing.item.Item;
import se.tjing.user.Person;

@Entity
public class Interaction extends BaseEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	private Item item;

	@ManyToOne
	private Person borrower;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusRequested;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusAccepted;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusHandedOver;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime statusReturned;

	@Override
	public Integer getId() {
		return id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Person getBorrower() {
		return borrower;
	}

	public void setBorrower(Person borrower) {
		this.borrower = borrower;
	}

	public DateTime getStatusRequested() {
		return statusRequested;
	}

	public void setStatusRequested(DateTime statusRequested) {
		this.statusRequested = statusRequested;
	}

	public DateTime getStatusAccepted() {
		return statusAccepted;
	}

	public void setStatusAccepted(DateTime statusAccepted) {
		this.statusAccepted = statusAccepted;
	}

	public DateTime getStatusHandedOver() {
		return statusHandedOver;
	}

	public void setStatusHandedOver(DateTime statusHandedOver) {
		this.statusHandedOver = statusHandedOver;
	}

	public DateTime getStatusReturned() {
		return statusReturned;
	}

	public void setStatusReturned(DateTime statusReturned) {
		this.statusReturned = statusReturned;
	}

}
