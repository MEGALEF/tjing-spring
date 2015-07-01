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
import se.tjing.feed.Notification;
import se.tjing.pool.Pool;
import se.tjing.pool.PoolRole;
import se.tjing.user.Person;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "pool", "member" }))
public class Membership extends TjingEntity {

	private PoolRole role = PoolRole.MEMBER;

	private Boolean approved = false;
	
	private Boolean hidden = false;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	
	@ManyToOne
	private Person member;

	@OneToMany(mappedBy="membership", cascade=CascadeType.ALL)
	private List<Notification> notifications;

	@OneToOne
	private Pool notifyPool = null;
	

	@OneToOne
	private Person notifyUser = null;

	@ManyToOne
	private Pool pool;

	public Membership() {
		// TODO Auto-generated constructor stub
	}
	
	public Membership(Person member, Pool pool) {
		this.member = member;
		this.pool = pool;
		this.role = PoolRole.MEMBER;
	}
	
	public Membership(Person member, Pool pool, boolean preapproved){
		this.member = member;
		this.pool = pool;
		this.approved = preapproved;
	}
	
	public void approve(){
		this.approved = true;
	}

	public Boolean getApproved() {
		return approved;
	}

	public Boolean getHidden() {
		return hidden;
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

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
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
