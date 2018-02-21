package stat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

final class Data { //just a data type
	private String name;
	private int score;

	public Data(String str, int num){
		name = str;
		score = num;
	}	
	public String getName(){
		return name;
	}
	public int getScore(){
		return score;
	}
}

public class Rank implements ActionListener{
	private FileReader fr;
	private FileWriter fw;
	private BufferedReader br;
	private BufferedWriter bw;
	private List<Data> database;
	private JFrame frame;
	private JLabel background;
	private final static int LABEL_WIDTH = 113;
	private final static int LABEL_HEIGHT = 30;
	private final static int START_X = 115;
	private final static int START_Y = 92;
	
	public Rank(){
		database = new ArrayList<Data>();
	}
	
	public void createWindow(){
		frame = new JFrame("Rank Table");
		frame.setLayout(null);
		initPanel();
		frame.setVisible(true);
		frame.setBounds(1000, 200, 404, 600);
		frame.setResizable(false);
	}
	
	public void initPanel(){
		List<JLabel> labels = new ArrayList<JLabel>();
		JLabel label;
		
		for(int i=0; i<13; i++){
			if( i>=database.size() )
				label = new JLabel("-------");
			else
				label = new JLabel( database.get(i).getName() );
			label.setForeground(Color.WHITE);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBounds(START_X+0*LABEL_WIDTH, START_Y+i*LABEL_HEIGHT, LABEL_WIDTH, LABEL_HEIGHT);
			labels.add(label);
			
			if( i>=database.size() )
				label = new JLabel("-------");
			else
				label = new JLabel( Integer.toString( database.get(i).getScore() ) );
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setBounds(START_X+1*LABEL_WIDTH, START_Y+i*LABEL_HEIGHT, LABEL_WIDTH, LABEL_HEIGHT);
			label.setForeground(Color.WHITE);
			labels.add(label);
		}
		
		for(JLabel varLabel:labels)
			frame.add(varLabel,0);
		JButton button = new JButton("Exit");
		button.setBackground(Color.LIGHT_GRAY);
		button.setBounds(START_X+23, START_Y+400, 140, 40);
		button.addActionListener(this);
		frame.add(button,0);

		try {
			background = new JLabel( new ImageIcon( ImageIO.read(getClass().getResourceAsStream("/res/rank/Background.png")) ));
		} catch (IOException e) {
			e.printStackTrace();
		}
		background.setBounds(0, 0, 404, 552);
		frame.add(background);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void readData() {
		try {
			File directory = new File("stat");
		    if (!directory.exists())
		        directory.mkdir();
			fr = new FileReader("stat/rank.txt");
			if (fr!=null) {
				br = new BufferedReader(fr);
				String str;
				String [] arr;
				while( (str=br.readLine())!=null ){
					arr = str.split("=");
					database.add( new Data(arr[0], Integer.parseInt(arr[1]) ) );
				}
				br.close();
				fr.close();
			}
		} catch (IOException e) {}
	}
	
	public void addData(String str, int num){
		database.add(new Data(str,num));
	}
	
	public void sortData(){
		Collections.sort(database, new Comparator<Data>(){
			@Override
			public int compare(Data o1, Data o2) {
				int score1 = ((Data)o1).getScore();
				int score2 = ((Data)o2).getScore();
				if( score1>score2 )
					return -1;
				else if( score1<score2 )
					return 1;
				else
					return 0;
			}
		});
	}
	
	public void writeData() throws IOException{
		int count = 1;
		fw = new FileWriter( "stat/rank.txt");
		bw = new BufferedWriter(fw);
		for (Data data: database) {
			bw.write(data.getName());
			bw.write("=");
			bw.write(Integer.toString(data.getScore()));
			bw.newLine();
			if (count>13) // 只保留前幾名記錄，以節省檔案大小
				break;
			else
				count++;
		}
		bw.close();
		fw.close();
	}
	
	public static void main(String [] args) throws IOException{
		Rank rank = new Rank();
		rank.readData();
		//rank.addData("Betty", 1000000);
		/*
		for(int i=999; i>0; i--)
			rank.addData("Alan",i);*/
		rank.sortData();
		rank.writeData();
		rank.createWindow();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}
