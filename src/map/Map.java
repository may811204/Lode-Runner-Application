package map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import main.Help;
import main.Model;
import processing.core.PApplet;
import processing.core.PImage;

public class Map {
	private final static int RECOVERY_COUNTER = 50;
	private static final int WIDTH = 20;
	private static final int HEIGHT = 11;
	private boolean firstTime = true;
	private PImage background = null;
	private Model model = null;
	private PApplet parent = null;
	private MapChunk [][] mapChunk;
	private HashMap<String, PImage> imageChunk;
	private List<Hole> holePool = new ArrayList<Hole>();
	private List<ch.Character> characters = new ArrayList<ch.Character>();
	
	@SuppressWarnings("serial")
	public Map (PApplet applet, int num) {
		parent = applet;
		model = new Model(num);
		mapChunk = model.constructMap();
		imageChunk = new HashMap<String, PImage>();
		for (String str: new ArrayList<String>() {{add("slippery"); add("solid"); add("spikes"); add("diggable"); add("ladder"); add("gold");}})
			imageChunk.put(str, Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/map/" + str + ".png")));
	}
	
	public void setBackground (PImage bg) {
		background = bg;
	}
	
	public void addCharacter (ch.Character c) {
		characters.add(c);
	}
	
	public Model getModel () {
		return model;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public void setChunk (int i, int j, MapChunk mc) {
		if( 0<=i && i<WIDTH && 0<=j && j<HEIGHT )
			mapChunk[i][j] = mc;
	}
	
	public MapChunk getChunk (int i, int j) {
		if( 0<=i && i<WIDTH && 0<=j && j<HEIGHT )
			return mapChunk[i][j];
		else
			return null;
	}
	
	public void setHole (int i, int j) {
		if (mapChunk[i][j].getType() == "diggable") {
			while(holePool.contains(new Hole(i, j, RECOVERY_COUNTER)))
				holePool.remove(new Hole(i, j, RECOVERY_COUNTER));
			mapChunk[i][j] = new Hole(i, j, RECOVERY_COUNTER);
			holePool.add((Hole)mapChunk[i][j]);
		}
	}

	public void holeRefresh () {
		List<Hole> deletePool = new ArrayList<Hole>();
		for (Hole h: holePool) {
			if (h.isTimeUp()) {
				deletePool.add(h);
				int i = h.getX();
				int j = h.getY();
				mapChunk[i][j] = new Ground(i, j, "diggable" );
				parent.image(imageChunk.get("diggable"), i*50, j*60);
			}
			else
				h.refresh();
		}
		holePool.removeAll(deletePool);
	}

	private void displayXY (int i, int j) {
		parent.image(background.get(i*50, j*60, 50, 60), i*50, j*60);
		if (mapChunk[i][j] != null) {
			PImage img = imageChunk.get(mapChunk[i][j].getType());
			if (img != null)
				parent.image(img, i*50, j*60);
		}
	}
	public void display () {
		if (firstTime) {
			for (int i=0; i<WIDTH; i++)
				for (int j=0; j<HEIGHT; j++)
					displayXY(i, j);
			firstTime = false;
		}
		for (Hole h: holePool)
			displayXY(h.getX(), h.getY());
		for (ch.Character c: characters)
			for (int i=c.getX()/50; i<=c.getX()/50+1; i++)
				for (int j=c.getY()/60; j<=c.getY()/60+1; j++)
					if (0<=i && i<WIDTH && 0<=j && j<HEIGHT)
						displayXY(i, j);
	}
	
	public Point getPlayerInitialLocation () {
		return model.getPlayerInitialLocation();
	}
	
	public List<Point> getEnemiesInitialLocation () {
		return model.getEnemiesInitialLocation();
	}
}

