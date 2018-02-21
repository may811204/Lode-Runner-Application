package ani;

import java.awt.Rectangle;
import processing.core.PApplet;
import processing.core.PImage;
import ch.Player;
import main.Help;

@SuppressWarnings("serial")
public class StartAnimation extends PApplet {
	private Player player;
	private boolean animationGo = false;
	private boolean animationOver = false;
	private Rectangle bound;
	private int a=0, b=0;
	private PImage forestimg = new PImage();
	private PImage startnoimg = new PImage();
	private PImage startimg = new PImage();
	private PImage title = new PImage();
	private PImage backStory = new PImage();
	private PImage mouse[] = new PImage[2];
	private boolean dataIsStored = false;
	private boolean story = false;

	public StartAnimation(Rectangle bounds){
		bound = bounds;
	}

	public void setup(){	
		player=new Player(this,290,340);
		forestimg = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/startAni/forest.jpg"));
		startnoimg = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/startAni/graystart.png"));
		title=Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/startAni/title.png"));
		startimg=Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/startAni/start.png"));
		backStory=Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/startAni/storyGround.png"));
		mouse[0]=Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/startAni/mouse.png"));
		mouse[1]=Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/startAni/white.png"));
		size(bound.width,bound.height);
		setLocation(0,0);
		background(255);
		frameRate(20);
	}

	public void draw(){
		if(story==false){
			image(forestimg,0,0);
			if(dataIsStored)
				image(startimg,200,450);
			else
				image(startnoimg,200,450);
			image(title,180,30);
			this.revalidate();
			this.repaint();
			if(animationGo==true){
				player.display();
				fill(0,0,0);
				rect(305,400,400,50);
				fill(0XFF,0XDC,35);
				rect(305,400,10*a,50);
				player.setMovement(3);
				player.move();
				player.move();
				if(a==40)
					story=true;	
				a++;
			}
		}
		else{
			image(backStory,0,0);
			image(mouse[(b%20<10)?1:0],850,400);
			b++;
		}
	}

	public void mouseClicked(){
		if(mouseX<=632 && mouseX>=350 && mouseY<=630&& mouseY>=500 && dataIsStored)
			animationGo=true;
		if(story==true && mouseX>=780 && mouseX<=950 && mouseY>=300 && mouseY<=400)
			animationOver=true;
	}

	public boolean isAnimationOver(){
		return animationOver;
	}

	public void setDataIsstored(boolean in){
		dataIsStored = in;
	}
}
