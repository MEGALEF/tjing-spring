package se.tjing.item;

import java.io.Serializable;

import se.tjing.user.Person;

public class AddItemRestObject implements Serializable {
	private String description;

	private String title;

	public Item buildItemWithOwner(Person owner) {
		Item item = new Item(owner, this.description, this.title);
		return item;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
