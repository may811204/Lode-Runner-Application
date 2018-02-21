package main;
//ÅªÀÉ¥Î

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import map.Gold;
import map.Ground;
import map.Ladder;
import map.MapChunk;

public class Model {

	private static final int WIDTH = 20;
	private static final int HEIGHT = 15;
	private int goldCount = 0;
	private BufferedReader bufferedReader;
	private String [][] map = new String[WIDTH][HEIGHT];
	private Point playerLoc = null;
	private List<Point> enemiesLoc = new ArrayList<Point>();
	
	public Model (int num) {
		try {
			InputStream configStream = getClass().getResourceAsStream("/res/gameStage/gameStage"+num+".txt");
			bufferedReader = new BufferedReader(new InputStreamReader(configStream, "UTF-8"));
		}
		catch (IOException e) {
			e.getStackTrace();
		}
	}
	/*
	 * buffertype[0] = Maptype;
	 * bufferPosition[0] = positionX;
	 * bufferPosition[1] = positionY;
	 * */
	
	public void decreaseGold () {
		goldCount--;
	}
	public int getGoldCount () {
		return goldCount;
	}

	private String[][] readData () {
		try {
			while(bufferedReader.ready()){
				String readLine;
				while((readLine = bufferedReader.readLine())!= null){
					String[] buffertype = readLine.split(":");
					String[] bufferPosition = (buffertype[1]).split(",");
					int Xposition = Integer.parseInt(bufferPosition[0])/50;
					int Yposition = Integer.parseInt(bufferPosition[1])/60;
					map[Xposition][Yposition] = buffertype[0].toString();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i=0;i<WIDTH;i++){
			for(int j=0;j<HEIGHT;j++){
				if(map[i][j]==null)
					map[i][j]=" ";
			}
		}
		return map;
	}

	public MapChunk[][] constructMap () {
		String [][] map = readData();
		MapChunk[][] mapChunk = new MapChunk[WIDTH][HEIGHT];
		for (int i=0; i<WIDTH; i++) {
			for (int j=0; j<HEIGHT; j++) {
				switch (map[i][j]) {
					case "slippery":
						mapChunk[i][j] = new Ground(i,j,"slippery");
						break;
					case "solid":
						mapChunk[i][j] = new Ground(i,j,"solid");
						break;
					case "spikes":
						mapChunk[i][j] = new Ground(i,j,"spikes");
						break;
					case "diggable":
						mapChunk[i][j] = new Ground(i,j,"diggable");
						break;
					case "gold":
						mapChunk[i][j] = new Gold(i,j);
						goldCount++;
						break;
					case "ladder":
						mapChunk[i][j] = new Ladder(i,j);
						break;
					case "player":
						playerLoc = new Point(i,j);
						break;
					case "enemy":
						enemiesLoc.add(new Point(i,j));
						break;
					default:
						break;
				}
			}
		}
		return mapChunk;
	}

	public Point getPlayerInitialLocation () {
		return playerLoc;
	}
	
	public List<Point> getEnemiesInitialLocation () {
		return enemiesLoc;
	}
}