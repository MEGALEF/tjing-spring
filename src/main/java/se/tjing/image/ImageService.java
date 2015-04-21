package se.tjing.image;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.exception.TjingException;
import se.tjing.item.Item;
import se.tjing.item.ItemRepository;
import se.tjing.user.Person;

@Service
public class ImageService {
	
	@Autowired
	ItemRepository itemRepo;

	@Autowired
	PictureRepository picRepo;
	
	public ItemPicture saveImage(Integer itemId, byte[] bytes, Person user) {
		Item item = itemRepo.findOne(itemId);
		if (!item.getOwner().equals(user)){
			throw new TjingException("Uploading pictures to other peoples stuff is rude!");
		} else {
			
			ByteArrayInputStream datastream = new ByteArrayInputStream(bytes);

			try { //TODO: move image resize to somewhere nice
				BufferedImage bimg;

				bimg = ImageIO.read(datastream);

				Image resized = bimg.getScaledInstance(100, 100, Image.SCALE_FAST);
				BufferedImage imgbuff = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
				imgbuff.getGraphics().drawImage(resized, 0, 0, new Color(0,0,0), null);
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				ImageIO.write(imgbuff, "jpg", buffer);

				ItemPicture picture = new ItemPicture(item, buffer.toByteArray());
				ItemPicture savedPic = picRepo.save(picture);

				return savedPic;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new TjingException(e.getMessage());
			}
		}
	}

	public byte[] getImage(Integer imgId) {
		//TODO User access restriction and stuff
		return picRepo.findOne(imgId).getData();
	}

}
