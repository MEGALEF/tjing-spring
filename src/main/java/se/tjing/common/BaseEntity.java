package se.tjing.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
}
