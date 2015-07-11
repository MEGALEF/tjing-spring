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
	protected DateTime creationTime;

	@Column(name = "modification_time", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonIgnore
	protected DateTime modificationTime;

	@Version
	@JsonIgnore
	private int version;

	public DateTime getCreationTime() {
		return creationTime;
	}

	public abstract Integer getId();

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
	
	
}
