package se.tjing.interaction;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.item.Item;
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
		if (!isPersonItemOwner(person, interaction)) {
			throw new TjingException("Only the item owner may do this");
		}
		interaction.setStatusAccepted(DateTime.now());
		Item item = interaction.getItem();
		item.setActiveInteraction(interaction);
		itemRepo.save(item);
		return interactionRepo.save(interaction);
	}

	public Interaction confirmHandover(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonBorrower(person, interaction)) {
			throw new TjingException(
					"Only the user who initially sent the request may do this");
		}
		interaction.setStatusHandedOver(DateTime.now());
		return interactionRepo.save(interaction);
	}

	public Interaction confirmReturn(Integer interactionId, Person person) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(person, interaction)) {
			throw new TjingException("Only the item owner may do this");
		}
		interaction.setStatusReturned(DateTime.now());
		interaction.getItem().setActiveInteraction(null);
		interaction.setActive(false);
		return interactionRepo.save(interaction);
	}

	public List<Interaction> getOutgoing(Person person) {
		QInteraction interaction = QInteraction.interaction;
		JPAQuery query = new JPAQuery(em);
		query.from(interaction).where(
				interaction.borrower.eq(person)
						.and(interaction.active.isTrue()));
		return query.list(interaction);
	}

	/**
	 * Gets a list of a users incoming interactions, ie interactions on items
	 * owned by the user
	 */
	public List<Interaction> getUserIncomingInteractions(Person person) {
		QInteraction interaction = QInteraction.interaction;
		QItem item = QItem.item;
		JPAQuery query = new JPAQuery(em).from(interaction)
				.leftJoin(interaction.item, item)
				.where(item.owner.eq(person).and(interaction.active.isTrue()));
		return query.list(interaction);
	}

	private boolean isPersonItemOwner(Person p, Interaction i) {
		if (!p.equals(i.getItem().getOwner())) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isPersonBorrower(Person p, Interaction i) {
		if (!p.equals(i.getBorrower())) {
			return false;
		} else {
			return true;
		}
	}

	public Interaction deny(Integer interactionId, Person currentUser) {
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(currentUser, interaction)) {
			throw new TjingException("Only the owner of the item may do this");
		}
		if (interaction.getStatusAccepted() != null) {
			throw new TjingException(
					"The interaction has already been accepted.");
		}
		interaction.setActive(false);
		return interactionRepo.save(interaction);
	}

	public List<Interaction> getUserInteractions(Person currentUser) {
		JPAQuery query = new JPAQuery(em);
		QInteraction interaction = QInteraction.interaction;
		QItem item = QItem.item;
		query.from(interaction).leftJoin(interaction.item, item)
		.where(interaction.borrower.eq(currentUser).or(item.owner.eq(currentUser))
				.and(interaction.active.isTrue()));
		return query.list(interaction);
	}

	public Interaction cancel(Integer interactionId, Person currentUser) { //TODO: Maybe this isn't useful
		Interaction interaction = interactionRepo.findOne(interactionId);
		if (!isPersonItemOwner(currentUser, interaction) && !isPersonBorrower(currentUser, interaction)){
			throw new TjingException("Only parties of the interaction may do this");
		} else {
			interaction.setStatusCancelled(DateTime.now());
			interaction.setActive(false);
			return interaction;
		}
		//TODO Cancel interactions even when item is handed over? Ok or not?
		
	}
}
