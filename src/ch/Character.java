package ch;

import main.Help;
import map.MapChunk;
import processing.core.PApplet;
import processing.core.PImage;

public class Character extends MapChunk {
	// for people to walk
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final int STAY = 4;
	protected static final int STEP = 5;
	private int frame, shift;
	private int movement;
	
	private PApplet parent;
	private PImage[] images;
	protected int velocity;

	public Character(PApplet parent, String[] imageLocation, int i, int j, int v) {
		this.parent = parent;
		this.images = loadImages(imageLocation);
		this.i = i;
		this.j = j;
		this.velocity = v;
		this.frame = 0;
		this.shift = 0;
	}
	private PImage[] loadImages(String[] location){
		PImage[] images = new PImage[location.length];
		for(int i=0; i<location.length; i++)
			images[i] = Help.loadPImageFromStream(this.getClass().getResourceAsStream(location[i]));
		return images;
	}
	public void display(){
		parent.image(this.images[shift + frame], this.i, this.j);
	}
	public void setMovement(int m){
		movement = m;
	}
	public int getMovement() {
		return movement;
	}
	public void move(){
		switch(movement) {
			case UP:
				this.j -= STEP;
				this.shift = 0;
				this.frame = (this.frame + 1)%2;
				break;
			case LEFT:
				this.i -= STEP;
				this.shift = 3;
				this.frame = (this.frame + 1)%3;
				break;
			case DOWN:
				this.j += STEP;
				this.shift = 2;
				this.frame = 0;
				break;
			case RIGHT:
				this.i += STEP;
				this.shift = 7;
				this.frame = (this.frame + 1)%3;
				break;
			default:
				this.shift = 6;
				this.frame = 0;
				break;
		}
	}

}
