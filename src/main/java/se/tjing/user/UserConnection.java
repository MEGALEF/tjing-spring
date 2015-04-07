package se.tjing.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="userconnection")
@IdClass(value=UserConnectionId.class)
public class UserConnection {
	//Achtung: Instances of this entity are created via JDBC via Spring Social. Currently it is being sort of jerry-rigged
	@Id
	@Column(name="userid")
	@JsonIgnore
	private String userId;
	
	@Id
	@Column(name="providerid")
	private String providerId;
	
	@Id
	@Column(name="provideruserid")
	private String providerUserId;

@JsonIgnore
	private Integer rank;
	
	private String displayname;
	
	private String profileurl;
	
	private String imageurl;
	
	@JsonIgnore
	@ManyToOne
	private Person person;
	
	@NotNull
	@JsonIgnore
	private String accesstoken;
	
	@JsonIgnore
	private String secret;
	
	@JsonIgnore
	private String refreshtoken;
	
	@JsonIgnore
	private Long expiretime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getProfileurl() {
		return profileurl;
	}

	public void setProfileurl(String profileurl) {
		this.profileurl = profileurl;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRefreshtoken() {
		return refreshtoken;
	}

	public void setRefreshtoken(String refreshtoken) {
		this.refreshtoken = refreshtoken;
	}

	public Long getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(Long expiretime) {
		this.expiretime = expiretime;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
