package se.tjing.rating;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import se.tjing.common.BaseEntity;
import se.tjing.interaction.Interaction;

@Entity
public class Rating extends BaseEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@OneToOne
	private Interaction interaction;
	private String ownerRating;
	private String borrowerRating;

	@Override
	public Integer getId() {
		return id;
	}

	public Interaction getTransaction() {
		return interaction;
	}

	public void setTransaction(Interaction transaction) {
		this.interaction = transaction;
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
