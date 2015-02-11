package se.tjing.item;

import se.tjing.user.Person;

import com.mysema.query.types.Predicate;

public class ItemPredicates {
	public static Predicate isOwner(Person p) {
		QItem item = QItem.item;
		return item.owner.eq(p);
	}

	public static Predicate itemMatches(String searchStr) {
		QItem item = QItem.item;
		return item.title.containsIgnoreCase(searchStr);
	}
}
