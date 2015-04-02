package se.tjing.common;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import se.tjing.feed.Notification;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class TjingEntity extends BaseEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Override
	public Integer getId() {
		return id;
	}
	
	@OneToMany(mappedBy="event")
	@JsonIgnore
	protected List<Notification<? extends TjingEntity>> notifications;
	
	private Boolean needsAction = false;

	public void addNotification(Notification<? extends TjingEntity> notification){
		this.notifications.add(notification);
	}
	
	public Boolean getNeedsAction() {
		return needsAction;
	}

	public List<Notification<? extends TjingEntity>> getNotifications() {
		return notifications;
	}
	
	public void setNeedsAction(Boolean needsAction) {
		this.needsAction = needsAction;
	}

	public void setNotifications(List<Notification<? extends TjingEntity>> notifications) {
		this.notifications = notifications;
	}
	
}
