package bar;

import main.Game;
import main.Help;
import java.awt.Rectangle;
import processing.core.PApplet;
import processing.core.PImage;

@SuppressWarnings("serial")
public class BottomBar extends PApplet {
	private Rectangle bound;
	private PImage bagImg = new PImage();
	private PImage emptyTreasureImg = new PImage();
	private PImage [][] treasure = new PImage[Game.MAX_LEVEL][3];
	private PImage [] treasureGet = new PImage[Game.MAX_LEVEL];

	public BottomBar (Rectangle bounds, int [] a) {
		initTreasure();
		bound = bounds;
		for (int i=0; i<Game.MAX_LEVEL; i++)
			treasureGet[i] = treasure[i][a[i]];
	}
	private void initTreasure () {
		bagImg = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/bottomBar/bag.png"));
		emptyTreasureImg = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/bottomBar/emptytreasure.jpg"));
		for (int i=0; i<Game.MAX_LEVEL; i++)
			for (int j=0; j<2; j++)
				treasure[i][j]= Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/bottomBar/data" + (i+1) + "_" + (j+1) + ".png"));
		for (int i=0; i<Game.MAX_LEVEL; i++)
			treasure[i][2]= emptyTreasureImg;
	}
	public void updateData (int [] a) {
		for (int i=0; i<Game.MAX_LEVEL; i++)
			treasureGet[i] = treasure[i][a[i]];
		this.redraw();
	}
	@Override
	public void setup () {
		this.setFocusable(false);
		size(bound.width, 50);
		setLocation(0,670);		
	}
	@Override
	public void draw () {
		background(63,90,108);
		image(bagImg,0,0);
		for (int i=0; i<Game.MAX_LEVEL; i++)
			image(treasureGet[i], 70+i*60, 0);
	}
}
