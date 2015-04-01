package se.tjing.item;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import se.tjing.common.BaseEntity;
import se.tjing.pool.Pool;
import se.tjing.user.Person;


@Entity
public class ItemRequest extends BaseEntity {

	@ManyToOne
	private Person user;
	
	private String text;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	@Override
	public Integer getId() {
		return id;
	}
}
