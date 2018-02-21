package ani;

import main.Game;
import main.Help;
import processing.core.PApplet;
import processing.core.PImage;

@SuppressWarnings("serial")
public class PassAnimation extends PApplet {
	private int prizeGet;
	private int a=0;
	private boolean check = false;
	private boolean chooseGift = false;
	private PImage backGround = new PImage();
	private PImage [] prize = new PImage[2];
	private PImage [][] datatest = new PImage[Game.MAX_LEVEL][2];
	private PImage clickMe = new PImage();
	private PImage beforeStage = new PImage();

	public PassAnimation(int stage){
		initDataTest();
		prize[0]=datatest[stage-2][0];
		prize[1]=datatest[stage-2][1];
		prizeGet = -1;
	}

	public void setup(){
		setSize(1000,700);
		backGround = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/passAni/passBackground.png"));
		clickMe = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/passAni/clickMe.png"));
		beforeStage = Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/passAni/passStoryGround.png"));
	}
	
	public void draw(){
		if(chooseGift==false){
			image(backGround,0,0);
			image(prize[0],150,225);
			image(prize[1],550,225);
			image(clickMe,200,550);
			image(clickMe,585,550);
		}
		else{
			stroke(256,256,256);
			background(256,256,256);
			image(beforeStage,0,50);
			if(a%20<=19&&a%20>=10)
				rect(780,300,200,100);
			a++;
		}
	}

	public void mouseClicked () {
		if (chooseGift==false && mouseX<=420 && mouseX>=200 && mouseY<=600 && mouseY>=550) {
			chooseGift = true;
			prizeGet = 0;
		}
		else if (chooseGift == false && mouseX<=850 && mouseX>=630 && mouseY<=600 && mouseY>=550) {
			chooseGift = true;
			prizeGet = 1;
		}
		else {}
		if (chooseGift &&  mouseX<=980 && mouseX>=780 && mouseY<=400 && mouseY>=300)
			check=true;
	}

	public boolean hasFinished () {
		return check;
	}

	public void initDataTest () {
		for (int i=0; i<Game.MAX_LEVEL; i++)
			for (int j=0; j<2; j++)
				datatest[i][j]=Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/passAni/datatest" + (i+1) + "_" + (j+1) + ".png"));
	}

	public int getPrizeGet () {
		return prizeGet;
	}
}
