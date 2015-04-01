package se.tjing.common;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import se.tjing.feed.Notification;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Petri Kainulainen
 */
@MappedSuperclass
public abstract class BaseEntity {

	@Column(name = "creation_time", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonIgnore
	private DateTime creationTime;

	@Column(name = "modification_time", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonIgnore
	private DateTime modificationTime;

	@Version
	@JsonIgnore
	private long version;
	
	@OneToMany(mappedBy="event")
	@JsonIgnore
	protected List<Notification<? extends BaseEntity>> notifications;
	
	private Boolean needsAction = false;

	public void addNotification(Notification<? extends BaseEntity> notification){
		this.notifications.add(notification);
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public abstract Integer getId();

	public DateTime getModificationTime() {
		return modificationTime;
	}

	public Boolean getNeedsAction() {
		return needsAction;
	}

	public List<Notification<? extends BaseEntity>> getNotifications() {
		return notifications;
	}

	public long getVersion() {
		return version;
	}

	@PrePersist
	public void prePersist() {
		DateTime now = DateTime.now();
		this.creationTime = now;
		this.modificationTime = now;
	}
	
	@PreUpdate
	public void preUpdate() {
		this.modificationTime = DateTime.now();
	}

	public void setNeedsAction(Boolean needsAction) {
		this.needsAction = needsAction;
	}

	public void setNotifications(List<Notification<? extends BaseEntity>> notifications) {
		this.notifications = notifications;
	}
}
