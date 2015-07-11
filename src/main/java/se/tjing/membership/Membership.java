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
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;

import se.tjing.common.TjingEntity;
import se.tjing.notification.Notification;
import se.tjing.pool.Pool;
import se.tjing.pool.PoolRole;
import se.tjing.user.Person;
import se.tjing.user.PostPerson;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "pool", "member" }))
public class Membership extends TjingEntity {

	private PoolRole role = PoolRole.MEMBER;

	private Boolean approved = false;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	
	@ManyToOne
	@NotNull
	private Person member;

	@OneToOne
	private Pool notifyPool = null;
	

	@OneToOne
	private Person notifyUser = null;

	@ManyToOne
	@NotNull
	private Pool pool;

	public Membership() {
	}
	
	public Membership(Person member, Pool pool) {
		this.member = member;
		this.pool = pool;
		this.role = PoolRole.MEMBER;
	}
	
	public Membership(Person member, Pool pool, boolean preapproved, PoolRole role){
		this.member = member;
		this.pool = pool;
		this.approved = preapproved;
		this.role = role;
	}

	public Boolean getApproved() {
		return approved;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Person getMember() {
		return member;
	}

	public Pool getPool() {
		return pool;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMember(Person member) {
		this.member = member;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	public PoolRole getRole() {
		return role;
	}

	public void setRole(PoolRole role) {
		this.role = role;
	}
}
