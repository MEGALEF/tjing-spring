package se.tjing.share;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import se.tjing.common.BaseEntity;
import se.tjing.condition.Condition;
import se.tjing.item.Item;
import se.tjing.pool.Pool;

@Entity
public class Share extends BaseEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	private Item item;
	@ManyToOne
	private Pool pool;

	@OneToOne
	private Condition condition;

	public Share(Item item, Pool pool) {
		this.item = item;
		this.pool = pool;
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
