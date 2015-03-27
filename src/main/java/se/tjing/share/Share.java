package se.tjing.share;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import se.tjing.common.BaseEntity;
import se.tjing.condition.Condition;
import se.tjing.item.Item;
import se.tjing.pool.Pool;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "pool", "item" }))
public class Share extends BaseEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	private Item item;
	@ManyToOne
	@JsonIgnoreProperties("shares")
	private Pool pool;

	@OneToOne
	private Condition condition;

	public Share(Item item, Pool pool) {
		this.item = item;
		this.pool = pool;
	}

	public Share() {

	}

	@Override
	public Integer getId() {
		return id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
