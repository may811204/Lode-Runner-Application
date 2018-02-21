package ch;

import processing.core.PApplet;

public class Player extends Character {
	
	private static final String[] imageLocation = createImageLocation();
	
	private static String[] createImageLocation(){
		String [] location = new String[10];
		location[0] = "/res/player/climbing1.png";
		location[1] = "/res/player/climbing2.png";
		location[2] = "/res/player/falling.png";
		location[3] = "/res/player/left1.png";
		location[4] = "/res/player/left2.png";
		location[5] = "/res/player/left3.png";
		location[6] = "/res/player/original.png";
		location[7] = "/res/player/right1.png";
		location[8] = "/res/player/right2.png";
		location[9] = "/res/player/right3.png";
		return location;
	}

	public Player(PApplet parent, int x, int y){
		super(parent, imageLocation,x,y,0);
		this.type="player";
		this.velocity=5;
		this.setMovement(STAY);
	}

	public void velChange(Player x){
		if(x.velocity==5)
			x.velocity=-5;
		else
			x.velocity=5;
	}
}
