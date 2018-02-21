package main;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import ch.Character;
import ch.Enemy;
import ch.Player;
import map.Ground;
import map.Ladder;
import map.Map;
import map.MapChunk;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class GameStage extends PApplet {

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 660;

	private List<Enemy> enemies = new ArrayList<Enemy>();
	private Player player;
	private Map map;

	private boolean fail = false;
	private Stack<Integer> keyPressedStack = new Stack<Integer>();
	private float refreshRate;

	public GameStage (int num, float rate) {
		map = new Map(this,num);
		refreshRate = rate;
	}
	@Override
	public void setup () {
		this.setBounds(0,50,1000,660);
		this.frameRate(refreshRate);
		player = new Player(this, 50*map.getPlayerInitialLocation().x, 60*map.getPlayerInitialLocation().y);
		map.addCharacter(player);
		map.setBackground(Help.loadPImageFromStream(this.getClass().getResourceAsStream("/res/gameStage/brightforest.png")));
		for (Point point: map.getEnemiesInitialLocation())
			enemies.add(new Enemy(this, 50*point.x, 60*point.y));
		for (Enemy enemy: enemies) {
			enemy.setMovement(Character.LEFT);
			map.addCharacter(enemy);
		}
	}
	public Map getMap () {
		return map;
	}

	// The main thread flow of each game stage
	@Override
	public void draw () {
		// Graphics Control
		map.display();

		// Moving Control
		shouldStopMovingCheck();
		playerMovingPart();
		enemiesMovingPart();

		// Mechanism Control
		eatGold();
		map.holeRefresh();
		
		// Death Control
		fallingIntoHoleDeath();
		onSpikesDeath();
		outOfBoundDeath();
	}

	/*******************************************
	 * Player Moving Control
	 *******************************************/
	private void shouldStopMovingCheck () {
		if (!canMoveLeft(player) && player.getMovement()==Character.LEFT
			|| !canMoveRight(player) && player.getMovement()==Character.RIGHT
			|| !canClimbUp(player) && player.getMovement()==Character.UP)
			player.setMovement(Character.STAY);
	}
	private void playerMovingPart () {
		if (!keyPressedStack.empty())
			setPlayerMovement(keyPressedStack.lastElement());
		else
			player.setMovement(Character.STAY);
		characterFallingPart(player);
		player.move();
		player.display();
	}
	private void enemiesMovingPart () {
		for (Enemy e : enemies) {
			if (fallingIntoHoleDeathCondition(e))
				e.setMovement(Character.STAY);
			else {
				e.setMovement(e.getMajorMovement());
				if ( e.getMajorMovement() == Character.LEFT &&
						(map.getChunk((e.getX())/50-1, (e.getY())/60) instanceof Ground || (map.getChunk((e.getX()+25)/50-1, (e.getY()+30)/60+1))==null)
					)
					e.setMajorMovement(Character.RIGHT);
				if ( e.getMajorMovement()== Character.RIGHT &&
						(map.getChunk((e.getX()+25)/50+1, (e.getY()+30)/60) instanceof Ground || (map.getChunk((e.getX()+25)/50+1, (e.getY()+30)/60+1))==null)
					)
					e.setMajorMovement(Character.LEFT);
				enemyEncounteredPart(e);
				characterFallingPart(e);
				e.move();
				e.display();
			}
		}
	}
	private void setPlayerMovement (int keyCode) {
		switch (keyCode) {
			case UP:
				if (canClimbUp(player))
					player.setMovement(Character.UP);
				break;
			case LEFT:
				if (canMoveLeft(player))
					player.setMovement(Character.LEFT);
				break;
			case DOWN:
				if (player.getY()<HEIGHT && isOnGroundApproximately(player)==false)
					player.setMovement(Character.DOWN);
				else if (player.getMovement() == Character.DOWN)
					player.setMovement(Character.STAY);
				break;
			case RIGHT:
				if (canMoveRight(player))
					player.setMovement(Character.RIGHT);
				break;
			default:
				break;
		}
	}
	private void characterFallingPart (Character c) {
		if (!keyPressedStack.contains(new Integer(DOWN)) && c.getMovement()==Character.DOWN && (isOnGround(c) || isOnLadder(c)))
			c.setMovement(Character.STAY);
		else if ( !isOnGround(c) && !isOnLadder(c) )
			c.setMovement(Character.DOWN);
		else if (c.getMovement()==Character.DOWN && isOnGroundApproximately(c))
			c.setMovement(Character.STAY);
		else {}
	}
	private void enemyEncounteredPart (Enemy e) {
		if (isCollidedWithEnemy(e))
			fail = true;
	}
	private boolean canClimbUp (Character c) {
		if (isOnLadder(c) && c.getY()>0) {
			if (25<=c.getX()%50 && c.getX()%50<=34 && map.getChunk(c.getX()/50, c.getY()/60) instanceof Ground) // left ground on the above
				return false;
			else if (15<=c.getX()%50 && c.getX()%50<=24 && map.getChunk(1+c.getX()/50, c.getY()/60) instanceof Ground) // right ground on the above
				return false;
			else if (c.getY()%60==0
					&& (map.getChunk((c.getX()+25)/50, c.getY()/60)==null || map.getChunk((c.getX()+25)/50, c.getY()/60).getType() != "ladder")) // no ladder above
				return false;
			else
				return true;
		}
		else
			return false;
	}
	private boolean canMoveLeft (Character c) {
		return c.getX() > 0
				&& (isOnGround(c)||isOnLadder(c))
				&& !(map.getChunk((c.getX()+0)/50-0, (c.getY()+10)/60) instanceof Ground)
				&& !(map.getChunk((c.getX()+0)/50-0, (c.getY()+30)/60) instanceof Ground)
				&& !(map.getChunk((c.getX()+0)/50-0, (c.getY()+55)/60) instanceof Ground);
	}
	private boolean canMoveRight (Character c) {
		return c.getX()<WIDTH
				&& (isOnGround(c)||isOnLadder(c))
				&& !(map.getChunk((c.getX()+0)/50+1, (c.getY()+10)/60) instanceof Ground)
				&& !(map.getChunk((c.getX()+0)/50+1, (c.getY()+30)/60) instanceof Ground)
				&& !(map.getChunk((c.getX()+0)/50+1, (c.getY()+55)/60) instanceof Ground);
	}
	private boolean isOnGround (Character c) {
		return (c.getY()%60==0) && (
					 	(c.getX()%50==0 && map.getChunk(c.getX()/50, 1+c.getY()/60) instanceof Ground)
					 || (c.getX()%50!=0 && (map.getChunk(c.getX()/50, 1+c.getY()/60) instanceof Ground || map.getChunk(1+c.getX()/50, 1+c.getY()/60) instanceof Ground)));
	}
	private boolean isOnGroundApproximately (Character c) {
		return (c.getY()%60==0) && (
					 	((c.getX()%50<=10 || c.getX()%50>=40) && map.getChunk((c.getX()+25)/50, 1+c.getY()/60) instanceof Ground)
					 || ((11<=c.getX()%50 && c.getX()%50<=39) && (map.getChunk(c.getX()/50, 1+c.getY()/60) instanceof Ground || map.getChunk(1+c.getX()/50, 1+c.getY()/60) instanceof Ground)));
	}
	private boolean isOnLadder (Character c) {
		return map.getChunk((c.getX()+25)/50, (c.getY()+45)/60) instanceof Ladder
				|| map.getChunk((c.getX()+25)/50, 1+c.getY()/60) instanceof Ladder;
	}
	private boolean isCollidedWithEnemy (Enemy e) {
		return Math.abs(e.getX()-player.getX())<=10 && Math.abs(e.getY()-player.getY())<=10;
	}

	/*******************************************
	 * Player Mechanism Control
	 *******************************************/
	private void eatGold () {
		if (map.getChunk((player.getX()+25)/50, (player.getY()+30)/60) != null) {
			if (map.getChunk((player.getX()+25)/50, (player.getY()+30)/60).getType() == "gold") {
				map.setChunk((player.getX()+25)/50, (player.getY()+30)/60, null);
				map.getModel().decreaseGold();
			}
		}
	}

	/*******************************************
	 * Player Death Control
	 *******************************************/
	private void onSpikesDeath () {
		MapChunk mchunk1 = map.getChunk(player.getX()/50, 1+player.getY()/60);
		MapChunk mchunk2 = map.getChunk(1+player.getX()/50, 1+player.getY()/60);
		if ((player.getY()%60==0) && (
			 	(player.getX()%50==0 && mchunk1!=null && mchunk1.getType()=="spikes")
			 || (player.getX()%50!=0 && (mchunk1!=null && mchunk1.getType()=="spikes" || mchunk2!=null && mchunk2.getType()=="spikes"))))
			fail = true;
	}
	private void outOfBoundDeath () {
		if (player.getX()<0 || player.getX()>WIDTH || player.getY()<0 || player.getY()>=HEIGHT )
			fail = true;
	}
	private void fallingIntoHoleDeath () {
		if (fallingIntoHoleDeathCondition(player)) {
			fail = true;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private boolean fallingIntoHoleDeathCondition (Character c) {
		return (c.getX()%50==0 && c.getY()%60==0 && map.getChunk( (c.getX()+25)/50, (c.getY()+30)/60 ) instanceof Ground);
	}

	/*******************************************
	 * Key Control
	 *******************************************/
	@Override
	public void keyReleased () {
		if (37<=keyCode && keyCode<=40)
			while(keyPressedStack.remove(new Integer(keyCode)));
	}
	@Override
	public void keyPressed () {
		if (37<=keyCode && keyCode<=40) {
			while(keyPressedStack.remove(new Integer(keyCode)));
			keyPressedStack.push(new Integer(keyCode));
		}
		else if (keyCode==KeyEvent.VK_N) {
			if (map.getChunk((player.getX()+25)/50-1, player.getY()/60+1) != null && map.getChunk((player.getX()+25)/50-1, player.getY()/60+1).getType() == "diggable")
				map.setHole((player.getX()+25)/50-1, (player.getY())/60+1);
		}
		else if (keyCode==KeyEvent.VK_M) {
			if (map.getChunk((player.getX()+25)/50+1, (player.getY())/60+1) != null && map.getChunk((player.getX()+25)/50+1, (player.getY())/60+1).getType() == "diggable")
				map.setHole((player.getX()+25)/50+1, player.getY()/60+1);
		}
	}

	/*******************************************
	 * Methods for changing stage flow control
	 *******************************************/
	public boolean hasFailed () {
		return fail;
	}
	public boolean hasWon () { /*already eat every gold*/
		return map.getModel().getGoldCount() == 0;
	}
}
