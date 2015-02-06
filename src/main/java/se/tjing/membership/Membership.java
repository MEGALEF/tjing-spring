package se.tjing.membership;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import se.tjing.pool.Pool;
import se.tjing.user.Person;

@Entity
public class Membership {
	@ManyToOne
	private Person member;
	@ManyToOne
	private Pool pool;

	public Person getMember() {
		return member;
	}

	public void setMember(Person member) {
		this.member = member;
	}

	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}
}
