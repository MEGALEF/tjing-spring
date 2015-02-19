package se.tjing.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.interaction.Interaction;
import se.tjing.interaction.InteractionRepository;
import se.tjing.user.Person;

@Service
public class RatingService {
	@Autowired
	RatingRepository ratingRepo;

	@Autowired
	InteractionRepository interactionRepo;

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

}
