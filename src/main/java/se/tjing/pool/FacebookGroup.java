package se.tjing.pool;

import java.util.Map;


public class FacebookGroup {
	
	private Map<String,Object> extraData;
	
	private Integer version;
	
	private String id;

	private String name;

	private int bookmarkOrder;

	private boolean administrator;

	private int unread;

	public FacebookGroup(){
	}

	public int getBookmarkOrder() {
		return bookmarkOrder;
	}

	public String getId() {
			return id;
		}

	public String getName() {
		return name;
	}

	public int getUnread() {
		return unread;
	}

public boolean isAdministrator() {
	return administrator;
}
	
	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}
	
	public void setBookmarkOrder(int bookmarkOrder) {
		this.bookmarkOrder = bookmarkOrder;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUnread(int unread) {
		this.unread = unread;
	}

	public Map<String,Object> getExtraData() {
		return extraData;
	}

	public void setExtraData(Map<String,Object> extraData) {
		this.extraData = extraData;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	

}
