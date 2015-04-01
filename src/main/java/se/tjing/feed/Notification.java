package se.tjing.feed;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.tjing.common.BaseEntity;
import se.tjing.user.Person;


@Entity
@Inheritance
@DiscriminatorColumn(name=Notification.NOTIFICATION_TYPE)
public abstract class Notification<T extends BaseEntity<Integer>> extends BaseEntity<Integer> {
	
	public static final String NOTIFICATION_TYPE = "notif_type";
	
	@JsonProperty
	public abstract String getNotificationType();

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	@ManyToOne
	private Person target;
	
	@ManyToOne
	private T event;
	
	private String message;
	
	public Notification(Person target, T event){
		this.target = target;
		this.event = event;
	}
	
	public Notification(){
		
	}

	public Person getTarget() {
		return target;
	}

	public void setTarget(Person target) {
		this.target = target;
	}

	public T getEvent() {
		return event;
	}

	public void setEvent(T event) {
		this.event = event;
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
