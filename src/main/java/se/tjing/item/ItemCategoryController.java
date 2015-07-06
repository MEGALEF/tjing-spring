package se.tjing.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/itemcategory")
public class ItemCategoryController {
	
	@Autowired 
	ItemCategoryService categoryService;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ItemCategory>> searchTags(@RequestParam(value="q") String q){
		List<ItemCategory> result;
		if (q==null){
			result = categoryService.getTags();
		} else {
			result = categoryService.search(q);
		}
		return new ResponseEntity<List<ItemCategory>>(result, null, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<ItemCategory> addTag(@RequestBody ItemCategory newCategory){
		ItemCategory result = categoryService.add(newCategory);
		return new ResponseEntity<ItemCategory>(result, null, HttpStatus.CREATED);
	}
}
