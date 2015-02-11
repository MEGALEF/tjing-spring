package se.tjing.condition;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import se.tjing.common.BaseEntity;
import se.tjing.share.Share;

@Entity
public class Condition extends BaseEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne
	private Share share;

	private String whatever; // TODO

	public String getWhatever() {
		return whatever;
	}

	public void setWhatever(String whatever) {
		this.whatever = whatever;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Share getShare() {
		return share;
	}

	public void setShare(Share share) {
		this.share = share;
	}

}
