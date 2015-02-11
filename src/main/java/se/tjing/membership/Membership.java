package se.tjing.membership;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import se.tjing.common.BaseEntity;
import se.tjing.pool.Pool;
import se.tjing.user.Person;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Membership extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@ManyToOne
	@JsonBackReference
	private Person member;
	@ManyToOne
	@JsonBackReference
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

	@Override
	public Object getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}