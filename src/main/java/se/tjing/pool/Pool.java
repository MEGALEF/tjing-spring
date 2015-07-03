package se.tjing.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.social.facebook.api.GroupMembership;

import se.tjing.common.BaseEntity;
import se.tjing.common.TjingEntity;
import se.tjing.membership.Membership;
import se.tjing.share.Share;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Pool extends TjingEntity {
	
	// closed by default
	private PoolRole approval = PoolRole.MEMBER; //By default members can approve joining members
	
	private String description;
	
	private Long facebookId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@OneToMany(mappedBy = "pool")
	@JsonIgnore
	private Set<Membership> memberships;

	private PrivacyMode privacy = PrivacyMode.OPEN;

	@OneToMany(mappedBy = "pool")
	@JsonIgnore
	private Set<Share> shares;
	private String title;
	
	public Pool(){
		
	}

	public Pool(GroupMembership gm){
		this.title = gm.getName();
		this.setFacebookId(Long.valueOf(gm.getId()));
		this.privacy = PrivacyMode.CLOSED;
	}

	public PoolRole getApproval() {
		return approval;
	}

	@JsonIgnore
	public List<Membership> getApprovedMemberships(){
		List<Membership> activemembers = new ArrayList<Membership>();
		for (Membership membership : this.memberships){
			if (membership.getApproved()==true){
				activemembers.add(membership);
			}
		}
		return activemembers;
	}

	public String getDescription() {
		return description;
	}

	public Long getFacebookId() {
		return facebookId;
	}

	public Integer getId() {
		return id;
	}
	
	public Set<Membership> getMemberships() {
		return memberships;
	}

	public PrivacyMode getPrivacy() {
		return privacy;
	}

	public String getTitle() {
		return title;
	}

	public void setApproval(PoolRole approval) {
		this.approval = approval;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFacebookId(Long facebookId) {
		this.facebookId = facebookId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMemberships(Set<Membership> memberships) {
		this.memberships = memberships;
	}

	public void setPrivacy(PrivacyMode mode) {
		this.privacy = mode;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Integer getNbrMembers(){
		return (memberships!=null) ? memberships.size() : 0;
	}
	
	public Integer getNbrShares(){
		return (shares!=null) ?  shares.size() : 0;
	}
}
