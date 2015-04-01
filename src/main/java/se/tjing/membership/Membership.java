package se.tjing.membership;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import se.tjing.common.BaseEntity;

import se.tjing.pool.Pool;
import se.tjing.pool.PrivacyMode;
import se.tjing.user.Person;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "pool", "member" }))
public class Membership extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@ManyToOne
	private Person member;
	

	@ManyToOne
	private Pool pool;

	@OneToOne
	private Pool notifyPool = null;

	@OneToOne
	private Person notifyUser = null;
	
	private Boolean approved = false;
	
	public Membership(Person member, Pool pool) {
		this.member = member;
		this.pool = pool;
	}

	public Membership() {
		// TODO Auto-generated constructor stub
	}

	public void approve(){
		this.approved = true;
	}

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
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
}
