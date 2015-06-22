package se.tjing.interaction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import se.tjing.common.TjingEntity;
import se.tjing.user.Person;

@Entity
public class InteractionMessage extends TjingEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String text;
	
	@JsonIgnore
	@ManyToOne
	private Interaction interaction;
	
	private boolean isSystemMessage = false;
	
	public InteractionMessage(){
		
	}

	public InteractionMessage(String msg, Interaction interaction) {
		this.text = msg;
		this.interaction = interaction;
	}
	
	public InteractionMessage(String msg, Interaction interaction, boolean isSystemMsg) {
		this.text = msg;
		this.interaction = interaction;
		this.isSystemMessage = isSystemMsg;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isSystemMessage() {
		return isSystemMessage;
	}

	public void setSystemMessage(boolean isSystemMessage) {
		this.isSystemMessage = isSystemMessage;
	}

}
