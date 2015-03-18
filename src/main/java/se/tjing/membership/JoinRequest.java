package se.tjing.membership;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import se.tjing.common.BaseEntity;
import se.tjing.pool.Pool;
import se.tjing.user.Person;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "pool", "member" }))
public class JoinRequest extends BaseEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	private Pool pool;

	@ManyToOne
	private Person member;

	public JoinRequest(Person person, Pool pool) {
		this.member = person;
		this.pool = pool;
	}

	public JoinRequest() {

	}

	@Override
	public Integer getId() {
		return id;
	}

	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	public Person getMember() {
		return member;
	}

	public void setMember(Person member) {
		this.member = member;
	}

}
