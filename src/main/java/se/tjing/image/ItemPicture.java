package se.tjing.image;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import se.tjing.common.BaseEntity;
import se.tjing.item.Item;

@Entity
public class ItemPicture extends BaseEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JsonIgnore
	private Item item;
	
	@JsonIgnore
	private byte[] data;
	
	//TODO This will go away when proper HATEOAS is implemented
	@JsonProperty
	public String getImageUrl(){
		return ImageController.URL + "/" + Integer.toString(this.id);
	}
	
	public ItemPicture(){
		
	}

	public ItemPicture(Item item, byte[] bytes) {
		this.item = item;
		this.data = bytes;
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Item getItem() {
		return item;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
}
