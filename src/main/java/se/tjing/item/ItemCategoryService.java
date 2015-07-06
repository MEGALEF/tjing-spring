package se.tjing.item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class ItemCategoryService {
	
	@Autowired
	ItemCategoryRepository categoryRepo;
	
	@Autowired
	EntityManager em;

	public List<ItemCategory> getTags() {
		List<ItemCategory> list = new ArrayList<ItemCategory>();
		for (ItemCategory category: categoryRepo.findAll()){
			list.add(category);
		}
		return list;
	}

	public List<ItemCategory> search(String searchStr) {
		JPAQuery query = new JPAQuery(em);
		QItemCategory table = QItemCategory.itemCategory;
		query.from(table).where(table.name.startsWithIgnoreCase(searchStr));

		return query.list(table);
	}
	
	public ItemCategory findOrAdd(String name){
		return add(new ItemCategory(name));
	}

	public ItemCategory add(ItemCategory newCategory) {
		JPAQuery query = new JPAQuery(em);
		QItemCategory table = QItemCategory.itemCategory;
		query.from(table)
		.where(table.name.equalsIgnoreCase(newCategory.getName()));
		ItemCategory existing = query.singleResult(table);
		if (existing == null){
			return categoryRepo.save(newCategory); 
		} else {
			return existing;
		}
	}
}
