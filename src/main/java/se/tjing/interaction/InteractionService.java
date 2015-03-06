package se.tjing.interaction;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.item.ItemRepository;
import se.tjing.item.QItem;
import se.tjing.user.Person;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class InteractionService {

	@Autowired
	EntityManager em;

	@Autowired
	InteractionRepository interactionRepo;

	@Autowired
	ItemRepository itemRepo;

	public Interaction accept(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		// TODO: Confirm user is owner
		interaction.setStatusAccepted(DateTime.now());
		return interactionRepo.save(interaction);
	}

	public Interaction confirmHandover(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		// TODO: confirm that user is borrower
		interaction.setStatusHandedOver(DateTime.now());
		return interactionRepo.save(interaction);
	}

	public Interaction confirmReturn(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		// TODO: Confirm that user is owner
		interaction.setStatusReturned(DateTime.now());
		return interactionRepo.save(interaction);
	}

	public Set<Interaction> getOutgoing(Person person) {
		return person.getInteractions();
	}

	/**
	 * Gets a list of a users incoming interactions, ie interactions on items
	 * owned by the user
	 */
	public List<Interaction> getUserIncomingInteractions(Person person) {
		QInteraction interaction = QInteraction.interaction;
		QItem item = QItem.item;
		JPAQuery query = new JPAQuery(em).from(interaction)
				.leftJoin(interaction.item, item).where(item.owner.eq(person));
		return query.list(interaction);
	}
}
