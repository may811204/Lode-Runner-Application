package main;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import processing.core.PConstants;
import processing.core.PImage;

public class Help {
	/************************************************************************************************
	 * Reference: https://forum.processing.org/one/topic/converting-bufferedimage-to-pimage#25080000000340208.html
	 ************************************************************************************************/
	public static PImage loadPImageFromStream(InputStream bis) {
		try {
			BufferedImage bimg = ImageIO.read(bis); 
			PImage img = new PImage(bimg.getWidth(),bimg.getHeight(),PConstants.ARGB);
			bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
			img.updatePixels();
			return img;
		}
		catch(Exception e) {
			System.err.println("Can't create image from buffer");
			e.printStackTrace();
		}
		return null;
	}
}
