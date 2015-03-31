package se.tjing.feed;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;

import se.tjing.interaction.QInteraction;
import se.tjing.membership.QMembership;
import se.tjing.pool.PoolService;
import se.tjing.pool.QPool;
import se.tjing.user.Person;

@Service
public class FeedService {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	PoolService poolService;

	public List<? extends FeedEvent> getFeed(Person currentUser) {
		QInteraction interaction = QInteraction.interaction;
		QPool pool = QPool.pool;
		QMembership membership = QMembership.membership;
		
		JPAQuery query = new JPAQuery(em);
		
		//Get Interaction events directly targeted at user
		query.from(interaction).where(interaction.notifyUser.eq(currentUser));
		List<? extends FeedEvent> interactionEvents = query.list(interaction);
		
		//Get join request events targeted at the users pools
		List<? extends FeedEvent> pendingMemberships;
		JPAQuery pendingMemberquery = new JPAQuery(em);
		pendingMemberquery.from(membership).where(membership.notifyPool.in(poolService.getUsersPools(currentUser)));
		pendingMemberships = pendingMemberquery.list(membership);
		
		//query.from(joinRequest).where(joinRequest.notifyUser.eq(currentUser)); 
		//List<? extends FeedEvent> joinRequests = query.list(joinRequest);
		
		//TODO: Add all attention-calling stuff here
		
		@SuppressWarnings("unchecked")
		List<? extends FeedEvent> allEvents = ListUtils.union(pendingMemberships, interactionEvents);
		
		return allEvents;
	}
}
