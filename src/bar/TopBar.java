package bar;

import main.Help;
import java.awt.Rectangle;
import processing.core.PApplet;
import processing.core.PImage;

@SuppressWarnings("serial")
public class TopBar extends PApplet {
	private int live = 5;
	private int score = 0;
	private int width;
	private PImage peopleImage = new PImage();
	private PImage moneyImage = new PImage();

	public TopBar (Rectangle bounds, int lives, int scores) {
		live = lives;
		score = scores;
		width = bounds.width;
	}

	public void setup () {
		this.setFocusable(false);
		size(width, 50);
		setLocation(0, 0);
		peopleImage = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/topBar/original.png"));
		moneyImage = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/topBar/littlegold.png"));
	}
	
	public void updateData (Integer lives, Integer scores) {
		if (lives!=null) live = lives;
		if (scores!=null) score = scores;
		this.redraw();
	}
	
	/*************************************************
	 * A method to initialize what top bar contains.
	 *************************************************/
	public void draw () {
		background(63,90,108);
		for (int a=0; a<live; a++)
			image(peopleImage,120+(30*a),0);
		image(moneyImage,300,0);
		textAlign(LEFT,LEFT);
		textSize(36);
		fill(256,0,0);
		text("Lives: ",0,40);
		fill(0,256,0);
		text(score,350,40);
	}
}
