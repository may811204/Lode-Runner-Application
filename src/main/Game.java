package main;
 
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import ani.GameOverAnimation;
import ani.GameWinAnimation;
import ani.PassAnimation;
import ani.StartAnimation;
import bar.BottomBar;
import bar.TopBar;
import stat.Rank;
import stat.Survey;

@SuppressWarnings("serial")
public class Game extends JFrame implements Runnable, ActionListener {

	// Constants
	private final static int DELAY = 20;
	private final static int RATE = 60;
	public final static int MAX_LEVEL = 10;

	// Variable Attributes
	private int lives = 5;
	private int score = 0;
	private int stageNumber = 1;
	private int problem = 1;
	private int goldRegister = 0;
	private int goldEatenInThisStage = 0;
	private String playerName = "Player";
	private String age = "0";
	private Thread gameThread = null;
	
	// GUI elements
	private TopBar topBar;
	private BottomBar bottomBar;
	private Rectangle bounds = new Rectangle(1000,750);
	private JButton button = new JButton("ENTER");
	private JTextArea [] inputAreas = new JTextArea[2];
	private JLabel [] inputLabels = new JLabel[2];
	private JLabel [] promptLabels = new JLabel[2];
	
	// Animations
	private PassAnimation passAnimation;
	private StartAnimation startAnimation;
	private GameOverAnimation gameOverAnimation = new GameOverAnimation();
	
	// Micellaneous
	private GameStage currentStage;
	private GameWinAnimation gameWin = new GameWinAnimation();
	private int [] picture = new int[MAX_LEVEL];
	private Rank rank = new Rank();
	private Survey survey = new Survey();
	private List<Integer> stageOrder = new ArrayList<Integer>();

	public Game() throws IOException {
		super();
		this.setVisible(false);
		this.setBounds(this.bounds);
		this.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (int i=0; i<MAX_LEVEL; i++)
			picture[i] = 2;
		for (int i=1; i<=MAX_LEVEL; i++)
			stageOrder.add(i);
		Collections.shuffle(stageOrder);
	}

	public void mainStart() {
		initStartAnimation();
		initArea();
		this.setVisible(true);
		if (gameThread == null)
            gameThread = new Thread(this);
		gameThread.start(); // execute run()
	}
	private void initStartAnimation () {
		startAnimation = new StartAnimation(bounds);
		startAnimation.init();
		startAnimation.start();
		this.add(startAnimation);
	}
	private void initArea() {
		inputAreas[0] = new JTextArea("Player");
		inputAreas[0].setEditable(true);
		inputAreas[0].setSize(400, 25);
		inputAreas[1] = new JTextArea("20");
		inputAreas[1].setEditable(true);
		inputAreas[1].setSize(400, 25);
		inputLabels[0] = new JLabel();
		inputLabels[0].add(inputAreas[0],0);
		inputLabels[0].setBounds(300, 250, inputAreas[0].getWidth(), inputAreas[0].getHeight());
		inputLabels[1] = new JLabel();
		inputLabels[1].add(inputAreas[1],0);
		inputLabels[1].setBounds(300, 300, inputAreas[1].getWidth(), inputAreas[1].getHeight());
		promptLabels[0] = new JLabel("Name: ");
		promptLabels[0].setBounds(250, 250, 40, 25);
		promptLabels[1] = new JLabel("Age: ");
		promptLabels[1].setBounds(250, 300, 40, 25);
		button.setBounds(725, 260, 100, 50);
		button.setBackground(Color.orange);
		button.addActionListener(this);
		this.add(inputLabels[0],0);
		this.add(inputLabels[1],0);
		this.add(promptLabels[0],0);
		this.add(promptLabels[1],0);
		this.add(button,0);
	}
	
	private boolean startAnimationOver = false;
	private boolean gameStageIsRunning = true;
	private boolean bottomBarUpdated = false;
	public void run() {
		try {
			while (Thread.currentThread() == gameThread) {
				long t = System.currentTimeMillis();
            	if (startAnimationOver == false) { // No other routines can run before the start animation finishes.
            		startAnimationOver = startAnimation.isAnimationOver();
					Thread.sleep(25);
            		if (startAnimationOver == true) // If the animation finishes, start running the new stage. 
            			startNewStageAfterStartAnimationOver();
            	}
            	else { // If the start animation finishes, other routines can run.
        		   	if (gameStageIsRunning) { // The case when the stage is still running
                     	if (lives > 0) { // If there are still some lives, the game runs as usual.
                     		// If the score has not been updated when the player eats one more gold, update the score.
                 			if (goldRegister != currentStage.getMap().getModel().getGoldCount()) 
                 				updateScore();
                     		if (currentStage.hasWon()) { // The case when the player wins this stage
                     			afterPlayerWinStage();
                     			gameStageIsRunning = false;
                     		}
                     		else if (currentStage.hasFailed()) {// The case when the player loses this stage
                     			afterPlayerLoseStage();
                     		}
                     		else {} // If the player neither wins nor loses, do nothing.
                     	}
                     	else { // If there is no any lives, the game is over.
                     		gameOver();
                     		break;
                     	}
                     	t += DELAY;
                        Thread.sleep(Math.max(0, t - System.currentTimeMillis()));
        		   	}
	                else { // The case when the stage is over
	                	Thread.sleep(500);
	                	if (bottomBarUpdated==false && passAnimation.getPrizeGet()>=0) { // Once a gift is chosen, update the bottom bar. 
	                		picture[stageNumber-2] = passAnimation.getPrizeGet();
	                		bottomBar.updateData(picture);
	                		bottomBarUpdated = true;
	                	}
	                	if (passAnimation.hasFinished()) { // If the choose-gift animation is over, prepare the new stage.
	                		if (stageNumber<=MAX_LEVEL) {
		                		prepareNewStageAfterOldStageOver();
		                		gameStageIsRunning = true;
		                		bottomBarUpdated = false;
	                		}
	                		else {
	                			prepareWinningProcedureAndWriteStatistics();
	                			break;
	                		}
	                	}
	                	else {} // Keep the choose-gift animation running.
	                } // end of >> if(gameStageIsRunning)
            	} // end of >> if (startAnimationOver == false)
			} // end of the while block
		} catch (Exception e) {
			e.printStackTrace();
		} // end of try-catch blocks
	}

	// 1st subroutine in run()
	private void startNewStageAfterStartAnimationOver () throws InterruptedException {
		this.remove(startAnimation);
		startAnimation.destroy();
		Thread.sleep(250);
		topBar = new TopBar(bounds,lives,score);
		topBar.init();
		topBar.start();
		bottomBar = new BottomBar(bounds,picture);
		bottomBar.init();
		bottomBar.start();
		this.add(topBar);
		this.add(bottomBar);
		createNewStage();
	}

	// 2nd subroutine in run()
	private void updateScore () {
		score += 100 * (goldRegister - currentStage.getMap().getModel().getGoldCount());
		goldEatenInThisStage += (goldRegister - currentStage.getMap().getModel().getGoldCount());
		goldRegister = currentStage.getMap().getModel().getGoldCount();
		topBar.updateData(null, score);
	}

	// 3nd subroutine in run()
	private void afterPlayerWinStage () throws IOException {
		goldEatenInThisStage = 0;
		this.remove(currentStage);
		currentStage.destroy();
		stageNumber++; // future stage number
		passAnimationInit();
	}
	
	// 3rd-1 subroutine in run()
	private void passAnimationInit() {
		passAnimation = new PassAnimation(stageNumber);
		passAnimation.init();
		passAnimation.start();
		this.add(passAnimation);
	}

	// 3rd-2 subroutine in run()
	private void prepareWinningProcedureAndWriteStatistics () throws IOException {
		this.remove(passAnimation);
		this.remove(topBar);
		this.remove(bottomBar);
		passAnimation.destroy();
		topBar.destroy();
		bottomBar.destroy();
		gameWin.init();
		gameWin.start();
		this.add(gameWin);
		rank.readData();
		rank.addData(playerName, score);
		rank.sortData();
		rank.writeData();
		rank.createWindow();
		survey.readData();
		survey.addData(Integer.parseInt(age), picture);
		survey.sortData();
		survey.writeData();
	}

	// 4th subroutine in run()
	private void afterPlayerLoseStage () {
		resetGameStage();
		lives--;
		score -= goldEatenInThisStage*100;
		goldEatenInThisStage = 0;
		goldRegister = currentStage.getMap().getModel().getGoldCount();
		topBar.updateData(lives, score);
	}

	// 4th-1 subroutine in run()
	private void resetGameStage () {
		this.remove(currentStage);
		currentStage.destroy();
		this.getContentPane().repaint();
		currentStage = new GameStage(problem,RATE);
		currentStage.init();
		currentStage.start();
		this.add(currentStage);
	}
	
	// 5th subroutine in run()
	private void gameOver() {
		this.remove(currentStage);
		this.remove(topBar);
		this.remove(bottomBar);
		currentStage.destroy();
		topBar.destroy();
		bottomBar.destroy();
		gameOverAnimation.start();
		this.add(gameOverAnimation);
		try {
			rank.readData();
			rank.addData(playerName, score);
			rank.sortData();
			rank.writeData();
			rank.createWindow();
			survey.readData();
			survey.addData(Integer.parseInt(age), picture);
			survey.sortData();
			survey.writeData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	// 6th subroutine in run()
	private void prepareNewStageAfterOldStageOver () {
		this.remove(passAnimation);
		passAnimation.destroy();
		createNewStage();
	}
	
	// 1st-1 & 6th-1 subroutine in run()
	private void createNewStage () {
		problem = stageOrder.get(0);
		stageOrder.remove(0);
		if (currentStage!=null) {
			this.remove(currentStage);
			currentStage.destroy();
		}
		currentStage = new GameStage(problem, RATE);
		currentStage.init();
		currentStage.start();
		goldRegister = currentStage.getMap().getModel().getGoldCount();
		this.add(currentStage);
	}

	// Action to do when pressing the OK button
	public void actionPerformed (ActionEvent event) {
		if (event.getSource() == button) {
			playerName = inputAreas[0].getText();
			age = inputAreas[1].getText();
			/* Check if the input data format is OK. */
			if (playerName.equals(""))
				return;
			try {
			    int num = Integer.parseInt(age);
			    if (num<0)
			    	return;
			    else
			    	startAnimation.setDataIsstored(true);
			} catch (NumberFormatException e) {
				return;
			}
			/***************************************/
			this.remove(button);
			this.remove(promptLabels[0]);
			this.remove(promptLabels[1]);
			this.remove(inputLabels[0]);
			this.remove(inputLabels[1]);
		}
	}
}
