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
	
	private String type = "generic";

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	@ManyToOne
	private Person target;
	
	private String message;
	
	public Notification(){
		
	}
	
	public Notification(Membership membership, Person target, String message){
		this.setMembership(membership);
		this.message = message;
		this.target = target;
	}
	
	public Notification(Interaction interaction, Person target, String message){
		this.setInteraction(interaction);
		this.message = message;
		this.target = target;
	}
	
	public Notification(ItemRequest itemrequest, Person target, String message){
		this.setItemrequest(itemrequest);
		this.message = message;
		this.target = target;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Membership getMembership() {
		return membership;
	}

	public void setMembership(Membership membership) {
		this.membership = membership;
		this.type = "membership";
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
		this.type = "interaction";
	}

	public ItemRequest getItemrequest() {
		return itemrequest;
	}

	public void setItemrequest(ItemRequest itemrequest) {
		this.itemrequest = itemrequest;
		this.type = "itemrequest";
	}

	@ManyToOne
	private Membership membership;
	@ManyToOne
	private Interaction interaction;
	@ManyToOne
	private ItemRequest itemrequest;

	public Person getTarget() {
		return target;
	}

	public void setTarget(Person target) {
		this.target = target;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
