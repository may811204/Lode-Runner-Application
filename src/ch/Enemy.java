package ch;

import processing.core.PApplet;

public class Enemy extends Character {
	private int majorMovement  = Character.LEFT;
	private static final String[] imageLocation = createImageLocation();
	private static String[] createImageLocation(){
		String [] location = new String[10];		
		location[0] = "/res/enemy/enemyleft1.png";
		location[1] = "/res/enemy/enemyleft1.png";
		location[2] = "/res/enemy/enemyleft1.png";
		location[3] = "/res/enemy/enemyleft1.png";
		location[4] = "/res/enemy/enemyleft2.png";
		location[5] = "/res/enemy/enemyleft3.png";
		location[6] = "/res/enemy/enemyleft1.png";
		location[7] = "/res/enemy/enemyright1.png";
		location[8] = "/res/enemy/enemyright2.png";
		location[9] = "/res/enemy/enemyright3.png";
		return location;
	}

	public Enemy(PApplet applet, int i, int j){
		super(applet, imageLocation, i, j, 5);
		this.i=i;
		this.j=j;
		this.type="enemy";
	}
	public void velChange(Enemy x){
		if(x.velocity==5)
			x.velocity=-5;
		else
			x.velocity=5;
	}
	public int getMovement() {
		return 0;
	}
	public void setMajorMovement(int i){
		majorMovement = i;
	}
	public int getMajorMovement(){
		return this.majorMovement;
	}
}
