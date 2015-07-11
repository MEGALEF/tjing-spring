package se.tjing.interactionmessage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import se.tjing.common.TjingEntity;
import se.tjing.interaction.Interaction;
import se.tjing.user.Person;

@Entity
public class InteractionMessage extends TjingEntity {
	
	@Column(name = "sent_time")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonIgnore
	private DateTime sentTime;
	
	@ManyToOne
	@JsonIgnoreProperties("connection")
	private Person author;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@JsonIgnoreProperties("conversation")
	@ManyToOne
	private Interaction interaction;
	
	@ManyToOne
	@JsonIgnoreProperties("connection")
	private Person recipient;
	
	public Person getRecipient() {
		return recipient;
	}

	public void setRecipient(Person recipient) {
		this.recipient = recipient;
	}

	private boolean isSystemMessage = false;
	
	private Boolean read = false;
	
	private String text;
	
	public InteractionMessage(){
		
	}
	
	public InteractionMessage(Person author, Person recipient, String msg, Interaction interaction) {
		this.text = msg;
		this.interaction = interaction;
		this.author = author;
		this.recipient = recipient;
	}

	public InteractionMessage(Person author, Person recipient, String msg, Interaction interaction, boolean isSystemMsg) {
		this.text = msg;
		this.interaction = interaction;
		this.isSystemMessage = isSystemMsg;
		this.author = author;
		this.recipient = recipient;
	}
	
	public Person getAuthor() {
		return author;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public Integer getInteractionId(){
		return interaction.getId();
	}

	public String getText() {
		return text;
	}

	public boolean isSystemMessage() {
		return isSystemMessage;
	}

	public void setAuthor(Person author) {
		this.author = author;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}

	public void setSystemMessage(boolean isSystemMessage) {
		this.isSystemMessage = isSystemMessage;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}
	
	@PrePersist
	public void prePersist() {
		DateTime now = DateTime.now();
		this.sentTime = now;
	}

}
