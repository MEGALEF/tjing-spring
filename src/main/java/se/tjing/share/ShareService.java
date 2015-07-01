package se.tjing.share;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.interaction.QInteraction;
import se.tjing.item.Item;
import se.tjing.item.ItemRepository;
import se.tjing.item.QItem;
import se.tjing.pool.Pool;
import se.tjing.pool.PoolRepository;
import se.tjing.user.Person;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class ShareService {
	
	@Autowired
	ShareRepository shareRepo;
	
	@Autowired
	ItemRepository itemRepo;
	
	@Autowired
	PoolRepository poolRepo;
	
	@Autowired
	EntityManager em;
	
	public Share shareToGroup(Person currentUser, NewShare newShare) {
		Pool pool = poolRepo.findOne(newShare.getPoolId());
		Item item = itemRepo.findOne(newShare.getItemId());

		if (!item.getOwner().equals(currentUser)){
			throw new TjingException("You must own the item you want to share");
		} else {
			JPAQuery query = new JPAQuery(em);
			query.from(QShare.share).where(QShare.share.item.eq(item).and(QShare.share.pool.eq(pool)));
			if (query.exists()){
				throw new TjingException("Share already exists");
			} else {
				Share share = new Share(item, pool);
				return shareRepo.save(share);
			}
		}
	}

	public void removeShare(Person currentUser, Integer shareId) {
		Share shareToRemove = shareRepo.findOne(shareId);
		Item item = shareToRemove.getItem();

		if (!item.getOwner().equals(currentUser)){
			throw new TjingException("Only the item owner may do this");
		} else {
			shareRepo.delete(shareToRemove);
		}
	}

	public List<Share> getShares(Person user) {
		JPAQuery query = new JPAQuery(em);
		QShare share = QShare.share;
		QItem item = QItem.item;
		
		query.from(share).leftJoin(share.item, item).where(item.owner.eq(user));
		return query.list(share);
	}
	
	public List<Share> getShares(Person user, Integer poolId){
		JPAQuery query = new JPAQuery(em);
		QShare share = QShare.share;
		QItem item = QItem.item;
		
		query.from(share).leftJoin(share.item, item)
		.where(item.owner.eq(user).and(share.pool.id.eq(poolId)));
		return query.list(share);
	}
}
