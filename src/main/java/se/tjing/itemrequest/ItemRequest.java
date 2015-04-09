package se.tjing.itemrequest;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.feed.notification.NotificationItemRequest;
import se.tjing.pool.Pool;
import se.tjing.user.Person;


@Entity
public class ItemRequest extends TjingEntity {

	@ManyToOne
	private Person user;
	
	private String text;

	@OneToMany(mappedBy="event", cascade=CascadeType.ALL)
	private List<NotificationItemRequest> notifications;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	public ItemRequest(AddItemRequest request, Person currentUser) {
		this.user = currentUser;
		this.text = request.getText();
	}

	@Override
	public Integer getId() {
		return id;
	}

	public List<NotificationItemRequest> getNotifications() {
		return notifications;
	}

	public String getText() {
		return text;
	}

	public Person getUser() {
		return user;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setNotifications(List<NotificationItemRequest> notifications) {
		this.notifications = notifications;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public void setUser(Person user) {
		this.user = user;
	}
}
