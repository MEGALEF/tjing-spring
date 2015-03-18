package se.tjing.rating;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.interaction.Interaction;
import se.tjing.interaction.InteractionRepository;
import se.tjing.user.Person;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class RatingService {
	@Autowired
	RatingRepository ratingRepo;

	@Autowired
	InteractionRepository interactionRepo;

	@Autowired
	EntityManager em;

	public Rating rate(Person user, Integer interactionId, String str) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		Rating rating = interaction.getRating();
		if (rating == null) {
			rating = new Rating();
			interaction.setRating(rating);
		}
		if (user.equals(interaction.getBorrower())) {
			rating.setBorrowerRating(str);
		} else if (user.equals(interaction.getItem().getOwner())) {
			rating.setOwnerRating(str);
		}
		return ratingRepo.save(rating);
	}

	/**
	 * Get a list of all ratings in which user person plays a role
	 * 
	 * @param person
	 * @return
	 */
	public List<Rating> getRatings(Person person) {
		JPAQuery query = new JPAQuery(em);
		QRating rating = QRating.rating;
		query.from(rating).where(
				rating.interaction.item.owner.eq(person).or(
						rating.interaction.borrower.eq(person)));
		return query.list(rating);
	}

}
