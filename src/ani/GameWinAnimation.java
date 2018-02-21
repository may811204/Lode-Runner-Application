package ani;

import main.Help;
import processing.core.PApplet;
import processing.core.PImage;

@SuppressWarnings("serial")
public class GameWinAnimation extends PApplet {
	private PImage [] jump = new PImage[2];
	private PImage moneyMountain = new PImage();
	private PImage reptile = new PImage();
	private PImage winTitle = new PImage();
	private int counter = 0;

	public void setup(){
		setSize(1000,700);
		setLocation(0,0);
		moneyMountain = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/gameWinAni/moneyMountain.png"));
		reptile = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/gameWinAni/deathreptile.png"));
		winTitle = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/gameWinAni/youwin.png"));
		jump[0]= Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/gameWinAni/Sara_jump1.png"));
		jump[1] = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/gameWinAni/Sara_jump2.png"));
	}

	public void draw(){
		image(moneyMountain,0,0);
		if ( 20<=counter%40 )
			image(jump[1], 500, 385);
		else
			image(jump[0], 500, 425);
		counter++;
		image(reptile,25,580);
		image(reptile,74,600);
		image(reptile,124,610);
		image(reptile,189,620);
		image(winTitle,200,0);
	}
}
