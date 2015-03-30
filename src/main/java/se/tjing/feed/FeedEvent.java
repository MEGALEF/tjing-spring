package se.tjing.feed;

import se.tjing.common.BaseEntity;
import se.tjing.pool.Pool;
import se.tjing.user.Person;

public abstract class FeedEvent extends BaseEntity<Integer>{
	public abstract Person getNotifyUser();
	
	public abstract Pool getNotifyPool();
	
	public abstract void setNotifyUser(Person user);
	
	public abstract void setNotifyPool(Pool pool);

}
