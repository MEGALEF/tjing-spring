package se.tjing.rating;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.interaction.Interaction;
import se.tjing.interaction.InteractionRepository;
import se.tjing.interaction.QInteraction;
import se.tjing.user.Person;
import se.tjing.user.PersonRepository;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class RatingService {
	@Autowired
	RatingRepository ratingRepo;

	@Autowired
	InteractionRepository interactionRepo;
	
	@Autowired
	PersonRepository personRepo;

	@Autowired
	EntityManager em;

	public Rating rate(Person user, AddRating newRating) {
		Interaction interaction = interactionRepo.findOne(newRating.getInteractionId());
		
		Rating rating = new Rating(newRating);
		
		if (user.equals(interaction.getBorrower())) {
			interaction.setBorrowerRating(rating);
		} else if (user.equals(interaction.getItem().getOwner())) {
			interaction.setOwnerRating(rating);
		}
		return ratingRepo.save(rating);
	}

	/**
	 * Get a list of all ratings in which user person plays a role
	 * 
	 * @param person
	 * @return
	 */
	public List<Rating> getRatings(Person person) { //
		JPAQuery query = new JPAQuery(em);
		QRating rating = QRating.rating;
		query.from(rating).where(
				rating.interaction.item.owner.eq(person).or(
						rating.interaction.borrower.eq(person)));
		return query.list(rating);
	}
	


	public List<Rating> getRatingsForUser(Integer userId) {
		Person user = personRepo.findOne(userId);
		return getRatings(user);
	}

}
