package se.tjing.membership;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import se.tjing.common.TjingEntity;
import se.tjing.notification.Notification;
import se.tjing.pool.Pool;
import se.tjing.pool.PoolRole;
import se.tjing.user.Person;
import se.tjing.user.PostPerson;


public class PostMembership{
	
	private PostPerson member;

	private Pool pool;

	public PostMembership() {
	}

	public PostPerson getMember() {
		return member;
	}

	public void setMember(PostPerson member) {
		this.member = member;
	}

	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}
	
}
