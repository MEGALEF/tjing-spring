package se.tjing.feed.notification;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonProperty;



@MappedSuperclass
public abstract class Notification<T extends TjingEntity> extends BaseEntity {
	//This class could be better, but unfortunately Hibernate doesn't seem to allow inheritance and generics at the same time like that. Fuck you Hibernate!
	
	//public static final String NOTIFICATION_TYPE = "notif_type";
	
	@JsonProperty
	public abstract String getNotificationType();

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	@ManyToOne
	protected Person target;
	
	protected String message;
	
	public Notification(){
		
	}
	
	@JsonProperty
	public abstract T getEvent();

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
