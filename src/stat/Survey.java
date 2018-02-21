package stat;

import java.awt.Color;
import java.awt.GridLayout;
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
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

final class AnalyzedData { //data used for the analyzed table
	private String name;
	private Integer [] data;
	
	public AnalyzedData(String str){
		name = str;
		data = new Integer [20];
		for(int i=0; i<data.length; i++)
			data[i] = 0;
	}
	public void addData(Integer [] arr){
		for(int i=0; i<arr.length; i++){
			if( arr[i]==0 )
				data[2*i] ++;
			else if( arr[i]==1 )
				data[2*i+1]++;
		}
	}
	public String getDataName(){
		return name;
	}
	public Integer [] getDataNumbers(){
		return data;
	}
}

final class SurveyData { //just a data type
	private int age;
	private Integer [] data;
	
	public SurveyData(int num, Integer [] arr){
		age = num;
		data = arr;
	}
	public int getAge(){
		return age;
	}
	public Integer[] getData(){
		return data;
	}
}

public class Survey implements ActionListener {
	private FileReader fr;
	private FileWriter fw;
	private BufferedReader br;
	private BufferedWriter bw;
	private List<SurveyData> database;
	private List<AnalyzedData> dataTable;
	private JFrame frame;
	private JTable table;
	private final static int WINDOW_WIDTH = 1100;
	private final static int WINDOW_HEIGHT = 393;
	private final static int VIEW_HEIGHT = 325;
	private final static int LABEL_HEIGHT = 30;
	
	public Survey(){
		database = new ArrayList<SurveyData>();
		dataTable = new ArrayList<AnalyzedData>();
	}
	
	public void createWindow(){
		frame = new JFrame("Survey Table");
		frame.setLayout(null);
		initPanel();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setBounds(100, 100, WINDOW_WIDTH+7, WINDOW_HEIGHT-10);
	}
	
	private void dataAnalyzing(List<SurveyData> list){
		for(int i=0; i<9; i++)
			dataTable.add(new AnalyzedData( (10*i) + "-" + (10*(i+1)-1 ) ) );
		dataTable.add(new AnalyzedData(">=90") );
		for(SurveyData data : list){
			int item;
			if( data.getAge()>=90 )
				item = 9;
			else
				item = data.getAge()/10;
			dataTable.get( item ).addData(data.getData());
		}
	}
	
	private void initPanel(){
		//Show the major table header
		JPanel majorColumn = new JPanel();
		String tableHeaderContext [] = {"Age","飲品","筆電","球鞋","速食","手錶","手機","武器","背包","樂器","球隊"};
		majorColumn.setBounds(0, 0, WINDOW_WIDTH, 30);
		majorColumn.setLayout(new GridLayout(1,11));
		JLabel [] columnLabels = new JLabel[11];
		for(int i=0; i<11; i++){
			columnLabels[i] = new JLabel( tableHeaderContext[i] );
			columnLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			columnLabels[i].setBorder( BorderFactory.createLineBorder(Color.BLACK) );
			majorColumn.add(columnLabels[i]);
		}
		frame.add(majorColumn);

		dataAnalyzing(database);
			
		//Show the data table
		String[] columns={"50嵐","橘子工坊","Acer","Asus","Adidas","Nike","肯德基","麥當勞","電子","石英","HTC","Apple","手榴彈","機關槍","後背","斜背","烏克麗麗","吉他","紅襪隊","洋基隊"};
		Integer [][] integerTable = new Integer[ dataTable.size() ][20];
		for(int i=0; i<dataTable.size(); i++)
			integerTable[i] = dataTable.get(i).getDataNumbers();
		table=new JTable(integerTable,columns);
		table.setEnabled(false);
		table.getTableHeader().setBorder( BorderFactory.createLineBorder(Color.BLACK) );
		//table.setBorder( BorderFactory.createLineBorder(Color.BLACK) );
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setRowHeight(30);
		JScrollPane jsp = new JScrollPane(table);
		jsp.setBounds(WINDOW_WIDTH/11, 30, WINDOW_WIDTH/11*10, VIEW_HEIGHT);
		frame.add(jsp);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT );
		table.setDefaultRenderer(String.class, centerRenderer);
		
		//Show the age column
		for(int i=0; i<dataTable.size(); i++){
			    JLabel label = new JLabel(  dataTable.get(i).getDataName() );
			    label.setHorizontalAlignment(SwingConstants.CENTER);
			    label.setBounds(0, 22+LABEL_HEIGHT*(i+1), WINDOW_WIDTH/11, LABEL_HEIGHT);
			    label.setBorder( BorderFactory.createLineBorder(Color.BLACK) );
			    frame.add(label);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void readData() {
		try {
			File directory = new File("stat");
		    if (!directory.exists())
		        directory.mkdir();
			fr = new FileReader("stat/survey.txt");
			if (fr!=null) {
				br = new BufferedReader(fr);
				String str;
				String [] arr;
				while( (str=br.readLine())!=null ){
					arr = str.split("=");
					Integer[] datas = new Integer[ arr.length-1 ];
					for(int i=1; i<arr.length; i++)
						datas[i-1] = Integer.parseInt(arr[i]);
					database.add( new SurveyData( Integer.parseInt(arr[0]), datas) );
				}
				br.close();
				fr.close();
			}
		} catch (IOException e) {}
	}
	
	public void addData(int num, int [] arr){
		Integer [] array = new Integer[arr.length];
		for(int i=0; i<arr.length; i++)
			array[i] = new Integer(arr[i]);
		database.add(new SurveyData(num,array));
	}
	
	public void sortData(){
		Collections.sort(database, new Comparator<SurveyData>(){
			@Override
			public int compare(SurveyData o1, SurveyData o2) {
				int score1 = ((SurveyData)o1).getAge();
				int score2 = ((SurveyData)o2).getAge();
				if( score1>score2 )
					return 1;
				else if( score1<score2 )
					return -1;
				else
					return 0;
			}
		});
	}
	
	public void writeData() throws IOException{
		fw = new FileWriter("stat/survey.txt");
		bw = new BufferedWriter(fw);
		for(SurveyData surveyData: database){
			bw.write( Integer.toString(surveyData.getAge()) );
			for(int j=0; j<surveyData.getData().length; j++){
				bw.write("=");
				bw.write( Integer.toString( surveyData.getData()[j] ) );
			}
			bw.newLine();
		}
		bw.close();
		fw.close();
	}
	
	public static void main(String [] args) throws IOException{
		Survey survey = new Survey();
		//Integer[] arr = {0,1,1,0,2,2,2,0,0,0};
		survey.readData();
		//survey.addData(125, arr);
		survey.sortData();
		survey.writeData();
		survey.createWindow();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}
