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
public abstract class BaseEntity<ID> {

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
	protected List<Notification<? extends BaseEntity<Integer>>> notifications;
	
	private Boolean needsAction = false;

	public abstract ID getId();

	public DateTime getCreationTime() {
		return creationTime;
	}

	public DateTime getModificationTime() {
		return modificationTime;
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

	public List<Notification<? extends BaseEntity<Integer>>> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification<? extends BaseEntity<Integer>>> notifications) {
		this.notifications = notifications;
	}
	
	public void addNotification(Notification<? extends BaseEntity<Integer>> notification){
		this.notifications.add(notification);
	}

	public Boolean getNeedsAction() {
		return needsAction;
	}

	public void setNeedsAction(Boolean needsAction) {
		this.needsAction = needsAction;
	}
}
