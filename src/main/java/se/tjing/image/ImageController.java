package se.tjing.image;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import se.tjing.exception.TjingException;
import se.tjing.user.PersonService;

@RestController
@RequestMapping(value=ImageController.URL)
public class ImageController {
	
	public static final String URL = "/image";

	@Autowired
	ImageService imageService;
	
	@Autowired
	PersonService personService;
	
	@RequestMapping(value ="{imgId}", method=RequestMethod.GET, produces=MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable Integer imgId){
		HttpHeaders headers = new HttpHeaders();
		byte[] imageData = imageService.getImage(imgId);
		return new ResponseEntity<byte[]>(imageData, headers, HttpStatus.OK);
	}
}
