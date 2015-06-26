package se.tjing.feed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.interaction.Interaction;
import se.tjing.itemrequest.ItemRequest;
import se.tjing.membership.Membership;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;



@Entity
@JsonInclude(Include.NON_NULL)
public class Notification extends BaseEntity {
	
	private EventType event;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	@ManyToOne
	private Interaction interaction;
	
	@ManyToOne
	private ItemRequest itemrequest;
	
	@ManyToOne
	private Membership membership;
	
	@ManyToOne
	private Person target;
	
	private NotificationType type = NotificationType.GENERIC;
	
	public Notification(){
		
	}
	
	public Notification(Interaction interaction, Person target, EventType event){
		this.setInteraction(interaction);
		this.target = target;
		this.event = event;
	}

	public Notification(ItemRequest itemrequest, Person target, EventType event){
		this.setItemrequest(itemrequest);
		this.target = target;
		this.event = event;
	}

	public Notification(Membership membership, Person target, EventType event){
		this.setMembership(membership);
		this.target = target;
		this.event = event;
	}

	public EventType getEvent() {
		return event;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public ItemRequest getItemrequest() {
		return itemrequest;
	}

	public Membership getMembership() {
		return membership;
	}

	public Person getTarget() {
		return target;
	}
	public NotificationType getType() {
		return type;
	}
	public void setEvent(EventType event) {
		this.event = event;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
		this.type = NotificationType.INTERACTION;
	}

	public void setItemrequest(ItemRequest itemrequest) {
		this.itemrequest = itemrequest;
		this.type = NotificationType.ITEMREQUEST;
	}

	public void setMembership(Membership membership) {
		this.membership = membership;
		this.type = NotificationType.MEMBERSHIP;
	}

	public void setTarget(Person target) {
		this.target = target;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}
}
