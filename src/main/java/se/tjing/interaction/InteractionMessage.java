package se.tjing.interaction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import se.tjing.common.TjingEntity;

@Entity
public class InteractionMessage extends TjingEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String text;
	
	@JsonIgnore
	@ManyToOne
	private Interaction interaction;
	
	public InteractionMessage(){
		
	}

	public InteractionMessage(IncomingMessage msg, Interaction interaction) {
		this.text = msg.getText();
		this.interaction = interaction;
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

}
