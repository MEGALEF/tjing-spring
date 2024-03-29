package se.tjing.rating;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.interaction.Interaction;
import se.tjing.user.Person;

@Entity
public class Rating extends TjingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@OneToOne
	private Interaction interaction;
	
	private RatingGrade grade;
	
	private String text;

	public Rating(AddRating newRating) {
		this.text = newRating.getText();
	}
	
	public Rating(){
		
	}

	@Override
	public Integer getId() {
		return id;
	}
	
	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}


	public void setText(String text) {
		this.text = text;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public String getText() {
		return text;
	}

	public RatingGrade getGrade() {
		return grade;
	}

	public void setGrade(RatingGrade grade) {
		this.grade = grade;
	}
}
