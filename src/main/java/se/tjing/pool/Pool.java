package se.tjing.pool;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import se.tjing.common.BaseEntity;
import se.tjing.membership.JoinRequest;
import se.tjing.membership.Membership;
import se.tjing.share.Share;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Pool extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String title;

	private PrivacyMode privacy = PrivacyMode.CLOSED; // Pool privacy set to
														// closed by default

	@OneToMany(mappedBy = "pool")
	@JsonIgnore
	private Set<Membership> memberships;

	@OneToMany(mappedBy = "pool")
	@JsonIgnore
	private Set<JoinRequest> requests;

	@OneToMany(mappedBy = "pool")
	@JsonIgnore
	private Set<Share> shares;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Membership> getMemberships() {
		return memberships;
	}

	public void setMemberships(Set<Membership> memberships) {
		this.memberships = memberships;
	}

	public PrivacyMode getMode() {
		return privacy;
	}

	public void setMode(PrivacyMode mode) {
		this.privacy = mode;
	}
}
