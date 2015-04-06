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

import se.tjing.feed.notification.Notification;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class TjingEntity extends BaseEntity{
	
	private Boolean needsAction = false;

	
	public Boolean getNeedsAction() {
		return needsAction;
	}

	
	public void setNeedsAction(Boolean needsAction) {
		this.needsAction = needsAction;
	}
	
}
