package se.tjing.feed;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;

import se.tjing.interaction.QInteraction;
import se.tjing.membership.QJoinRequest;
import se.tjing.membership.QMembership;
import se.tjing.pool.QPool;
import se.tjing.user.Person;

@Service
public class FeedService {
	
	@Autowired
	EntityManager em;

	public List<? extends FeedEvent> getFeed(Person currentUser) {
		QInteraction interaction = QInteraction.interaction;
		QJoinRequest joinRequest = QJoinRequest.joinRequest;
		QPool pool = QPool.pool;
		QMembership membership = QMembership.membership;
		
		JPAQuery query = new JPAQuery(em);
		
		//Get Interaction events directly targeted at user
		query.from(interaction).where(interaction.notifyUser.eq(currentUser));
		List<? extends FeedEvent> interactionEvents = query.list(interaction);
		
		//Get join request events targeted at the users pools
		query.from(joinRequest).leftJoin(joinRequest.pool, pool).leftJoin(pool.memberships, membership)
		.where(membership.member.eq(currentUser));
		List<? extends FeedEvent> joinRequests = query.list(joinRequest);
		
		//query.from(joinRequest).where(joinRequest.notifyUser.eq(currentUser)); 
		//List<? extends FeedEvent> joinRequests = query.list(joinRequest);
		
		//TODO: Add all attention-calling stuff here
		
		@SuppressWarnings("unchecked")
		List<? extends FeedEvent> allEvents = ListUtils.union(joinRequests, interactionEvents);
		
		return allEvents;
	}
}
