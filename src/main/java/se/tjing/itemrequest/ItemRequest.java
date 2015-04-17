package se.tjing.itemrequest;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import se.tjing.common.TjingEntity;
import se.tjing.feed.notification.Notification;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class ItemRequest extends TjingEntity {

	@ManyToOne
	private Person member;
	
	private String text;

	@OneToMany(mappedBy="itemrequest", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<Notification> notifications;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	public ItemRequest(AddItemRequest request, Person currentUser) {
		this.member = currentUser;
		this.text = request.getText();
	}
	
	public ItemRequest(){
		
	}

	@Override
	public Integer getId() {
		return id;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public String getText() {
		return text;
	}

	public Person getUser() {
		return member;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public void setUser(Person user) {
		this.member = user;
	}
}
