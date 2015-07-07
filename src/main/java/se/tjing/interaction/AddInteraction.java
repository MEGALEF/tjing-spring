package se.tjing.interaction;

public class AddInteraction {
	private Integer itemId;
	
	public AddInteraction(Integer itemId){
		this.itemId = itemId;
	}
	
	public String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AddInteraction(){
		
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

}
