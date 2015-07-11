package se.tjing.notification;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.interaction.Interaction;
import se.tjing.interactionmessage.InteractionMessage;
import se.tjing.itemrequest.ItemRequest;
import se.tjing.membership.Membership;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;



@Entity
@JsonInclude(Include.NON_NULL)
public class Notification extends BaseEntity {
	
	private Boolean read = false;
	
	private EventType event;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Interaction interaction;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private ItemRequest itemrequest;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Membership membership;
	
	@ManyToOne
	private Person target;
	
	private NotificationType type = NotificationType.GENERIC;

	@ManyToOne
	private InteractionMessage message;
	
	public Notification(){
		
	}
	
	public Notification(Interaction interaction, Person target, EventType event){
		this.setInteraction(interaction);
		this.target = target;
		this.event = event;
	}

	public Notification(InteractionMessage msg, Person recipient,
			EventType intMessage) {
		this.setMessage(msg);
		this.target = recipient;
		this.event = intMessage;
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

	public InteractionMessage getMessage() {
		return message;
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

	public void setMessage(InteractionMessage message) {
		this.message = message;
		this.type = NotificationType.MESSAGE;
	}

	public void setTarget(Person target) {
		this.target = target;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}
}
