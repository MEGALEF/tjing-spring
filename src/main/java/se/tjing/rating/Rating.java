package se.tjing.rating;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import se.tjing.common.BaseEntity;
import se.tjing.interaction.Interaction;

public class Rating extends BaseEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	private Interaction transaction;
	private String ownerRating;
	private String borrowerRating;

	@Override
	public Integer getId() {
		return id;
	}

	public Interaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Interaction transaction) {
		this.transaction = transaction;
	}

	public String getOwnerRating() {
		return ownerRating;
	}

	public void setOwnerRating(String ownerRating) {
		this.ownerRating = ownerRating;
	}

	public String getBorrowerRating() {
		return borrowerRating;
	}

	public void setBorrowerRating(String borrowerRating) {
		this.borrowerRating = borrowerRating;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
