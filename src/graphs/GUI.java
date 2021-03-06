package graphs;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.JCheckBox;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import java.awt.Font;
import java.util.List;

import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import com.google.common.collect.Lists;
import db_wrappers.DBConnection;
import settings.DBSettings;

public class GUI {

	private JFrame frame;
	private JFrame dimensions;
	private JFrame measures;
	private JFrame dbconnection;
	private DBSettings dbsettings;
	private DBConnection connection;
	private final JPanel panel = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private List<String> userMeasures = new ArrayList<String>();
	private List<String> userDimensions = new ArrayList<String>();
	List<String> integerColumns = new ArrayList<String>();
	private List<String> tables = new ArrayList<String>();
	private String query = null;
	private String table = null;
	private String column = null;
	private String value = null;
	private String operator = null;
	private String aggregrate = null;
	private Integer binValue = 0;
	private Boolean normalise;
	private Boolean weighted;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initializeConnection(0);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeMainGUI() {
		frame = new JFrame("SeeDB");
		frame.setBounds(100, 100, 620,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);

		panel_2.setBounds(0, 280, 603, 177);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblVisualisationBuilder = new JLabel("Aggregrate Function");
		lblVisualisationBuilder.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblVisualisationBuilder.setBounds(90, 0, 250, 30);
		panel_2.add(lblVisualisationBuilder);
		
		JRadioButton sum = new JRadioButton("Sum");
		sum.setFont(new Font("Tahoma", Font.PLAIN, 18));
		sum.setBounds(215, 50, 100, 20);
		panel_2.add(sum);
		
		JRadioButton count = new JRadioButton("Count");
		count.setBounds(215, 90, 100, 20);
		count.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(count);
		
		JRadioButton all = new JRadioButton("All");
		all.setFont(new Font("Tahoma", Font.PLAIN, 18));
		all.setBounds(70, 50, 100, 20);
		panel_2.add(all);
		
		JRadioButton avg = new JRadioButton("Average");
		avg.setBounds(70, 90, 100, 20);
		avg.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(avg);
		
		JLabel lblBin = new JLabel("Bin Value");
		lblBin.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblBin.setBounds(360, 5, 150, 30);
		panel_2.add(lblBin);
		
		JTextField textBin = new JTextField();
		textBin.setBounds(450, 5, 80, 30);
		textBin.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(textBin);
		
		JCheckBox normaliseCheckBox = new JCheckBox("Normalise");
		normaliseCheckBox.setBounds(360, 45, 200, 30);
		normaliseCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		normaliseCheckBox.setSelected(false);
        panel_2.add(normaliseCheckBox);
        
        JCheckBox weightedCheckBox = new JCheckBox("Weighted");
		weightedCheckBox.setBounds(360, 85, 200, 30);
		weightedCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		weightedCheckBox.setSelected(false);
        panel_2.add(weightedCheckBox);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(avg);
	    group.add(sum);
	    group.add(count);
	    group.add(all);
	    
	    all.setSelected(true);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 0, 650, 285);
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblQuery = new JLabel("Database");
		lblQuery.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblQuery.setBounds(250, 5, 210, 40);
		panel_3.add(lblQuery);
		
		JLabel lblDataset = new JLabel("Dataset");
		lblDataset.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDataset.setBounds(59, 51, 69, 20);
		panel_3.add(lblDataset);
		
		tables = connection.getTables();
		JComboBox list_3 = new JComboBox(tables.toArray());
		list_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		list_3.setBounds(138, 47, 185, 30);
		panel_3.add(list_3);
			
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(15, 90, 603, 200);
		panel_3.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblFilter = new JLabel("Query");
		lblFilter.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFilter.setBounds(250, 15, 210, 40);
		panel_4.add(lblFilter);
		
		JLabel lblAttribute = new JLabel("Attribute");
		lblAttribute.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAttribute.setBounds(44, 61, 69, 30);
		panel_4.add(lblAttribute);
		
		JComboBox<String> list_4 = new JComboBox<>();
		list_4.setBounds(123, 61, 185, 30);
		list_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_4.add(list_4);
		
		JLabel lblOperator = new JLabel("Operator");
		lblOperator.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblOperator.setBounds(44, 97, 69, 30);
		panel_4.add(lblOperator);
		
		JComboBox<String> list_5 = new JComboBox<>();
		list_5.setFont(new Font("Tahoma", Font.PLAIN, 18));

		list_5.setBounds(123, 97, 185, 30);
		panel_4.add(list_5);
		
		JLabel lblValue = new JLabel("Value");
		lblValue.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblValue.setBounds(44, 133, 69, 30);
		panel_4.add(lblValue);
		
		JComboBox<String> list_6 = new JComboBox<>();
		list_6.setFont(new Font("Tahoma", Font.PLAIN, 18));
		list_6.setBounds(123, 133, 185, 30);
		panel_4.add(list_6);
				
		JButton btnGetRecommendations = new JButton("Get Results");
		btnGetRecommendations.setBounds(351, 97, 161, 29);
		panel_4.add(btnGetRecommendations);
		btnGetRecommendations.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	value = (String) list_6.getSelectedItem();
		     	operator = (String) list_5.getSelectedItem();
		     	query = "SELECT * FROM " + table + " WHERE " + column + operator 
		     			+ "'" + value + "'";
		     	normalise = normaliseCheckBox.isSelected();
		     	weighted = weightedCheckBox.isSelected();
		     	if (sum.isSelected()){
		     		aggregrate = "SUM";
		     	} else if (avg.isSelected()){
		     		aggregrate = "AVG";
		     	} else if (count.isSelected()){
		     		aggregrate = "COUNT";
		     	} else if (all.isSelected()){
		     		aggregrate = "ALL";
		     	}
		     	try {
		     		binValue = Integer.parseInt(textBin.getText());
		     	} catch (Exception e1){
		     		e1.printStackTrace();
		     	}
		     	System.out.println(aggregrate);
	        	if (dbsettings == null) {
	        		JOptionPane.showMessageDialog(frame, "Please Specify A Database");
		     	} else if (query == null){
		     		JOptionPane.showMessageDialog(frame, "Please Specify A Dataset");
		     	} else {
		     		initializeDimensions(graph.getColumns(dbsettings, table));
		     	}
	         }
	      });
		
		JButton btnAddDataset = new JButton("Edit Database");
		btnAddDataset.setBounds(366, 48, 161, 29);
		panel_3.add(btnAddDataset);
		btnAddDataset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				initializeConnection(1);
			}
		});
		
		list_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				list_4.removeAllItems();
				table = (String) list_3.getSelectedItem();
				for (String temp : graph.getColumns(dbsettings, table)){
					list_4.addItem(temp);
				}
				System.out.println(table);
			}
		});
		
		list_4.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	column = (String) list_4.getSelectedItem();
	        	list_6.removeAllItems();
	        	if (connection.getIntegerColumns("SELECT " + column + " FROM " + table).size() != 0){
	        		list_5.removeAllItems();
	        		list_5.addItem("=");
	        		list_5.addItem("<>");
	        		list_5.addItem(">");
	        		list_5.addItem("<");
	        		list_5.addItem(">=");
	        		list_5.addItem("<=");
	        	} else {
	        		list_5.removeAllItems();
	        		list_5.addItem("=");
	        		list_5.addItem("<>");
	        	}
	        	for (String temp : connection.getUniqueValues(column, table)){
					list_6.addItem(temp);
				}
	         }
	      });
		
		for(ActionListener a: list_3.getActionListeners()) {
		    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		}
		
		for(ActionListener a: list_4.getActionListeners()) {
		    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		}
	}
	
	private void initializeConnection(int indicator) {
		dbconnection = new JFrame("Database Connection");
		dbconnection.setBounds(200, 100, 500, 400);
		if (indicator == 0) {
			dbconnection.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		dbconnection.getContentPane().setLayout(null);
		JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 600, 500);
        
        JPanel hostAddress = new JPanel();
        hostAddress.setLayout(null);
        hostAddress.setBounds(0, 0, 600, 500);
        
		JLabel lblHost = new JLabel("Host Address");
		lblHost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblHost.setBounds(50, 50, 200, 20);
		hostAddress.add(lblHost);
		
		JTextField textHost = new JTextField();
		textHost.setBounds(200, 45, 200, 30);
		textHost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		hostAddress.add(textHost);
		     
        JLabel lblType = new JLabel("Database Type");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblType.setBounds(50, 100, 200, 20);
		hostAddress.add(lblType);
		
		JComboBox<String> textType = new JComboBox<String>();
		textType.addItem("postgresql");
		textType.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textType.addItem("vertica");
		textType.setBounds(200, 95, 200, 30);
		hostAddress.add(textType);
		      
        JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsername.setBounds(50, 150, 200, 20);
		hostAddress.add(lblUsername);
		
		JTextField textUsername = new JTextField();
		textUsername.setBounds(200, 145, 200, 30);
		textUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
		hostAddress.add(textUsername);
        
        JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPassword.setBounds(50, 200, 200, 20);
		hostAddress.add(lblPassword);
		
		JPasswordField textPassword = new JPasswordField();
		textPassword.setEchoChar('*');
		textPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textPassword.setBounds(200, 195, 200, 30);
		hostAddress.add(textPassword);
		
		JButton btnAddDatabaseSettings = new JButton("Set Database");
		btnAddDatabaseSettings.setBounds(75, 275, 150, 30);
		hostAddress.add(btnAddDatabaseSettings);
		btnAddDatabaseSettings.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	connection = new DBConnection();
	        	DBSettings tempdbsettings = new DBSettings(textHost.getText(), 
	        			(String) textType.getSelectedItem(),
	        			textUsername.getText(), new String(textPassword.getPassword()));
	     		if (!connection.hasConnection()) {
	     			System.out.println("DB connection not specified, using default connection params");
	     			// populate connection from settings
	     			if (!connection.connectToDatabase(tempdbsettings)) {
	     				JOptionPane.showMessageDialog(dbconnection, "Cannot Connect to Database");
	     				//System.out.println("Cannot connect to DB with default settings");
	     			} else {
	     				dbsettings = tempdbsettings;
	     				initializeMainGUI();
	     				dbconnection.setVisible(false);
	     			}
	     		}
	         }          
	      });
		
		JButton btnCloseDatabaseSettings = new JButton("Close");
		btnCloseDatabaseSettings.setBounds(275, 275, 150, 30);
		hostAddress.add(btnCloseDatabaseSettings);
		btnCloseDatabaseSettings.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 if (indicator == 0) {
	        		 System.exit(0);
	        	 } else if (indicator == 1){
	        		 dbconnection.setVisible(false);
	        	 }
		     }          
		});
		mainPanel.add(hostAddress);
		dbconnection.getContentPane().add(mainPanel);
        dbconnection.setVisible(true);
	}
	
	private void initializeDimensions(java.util.List<String> list) {
		dimensions = new JFrame("Dimensions");
		integerColumns = graph.getIntegerColumns(dbsettings, query);
		List<String> possibleDimensions = new ArrayList<String>();
		for (int i=0; i < list.size(); i++){
			if (connection.getNullColumn(list.get(i), table) == 0 && 
					!(integerColumns.contains(list.get(i)) && binValue <= 0 
					&& connection.getNumUniqueValues(list.get(i), table) > 10)){
				possibleDimensions.add(list.get(i));
			}
		}
		possibleDimensions.remove(column);
		dimensions.setBounds(200, 100, 750, ((possibleDimensions.size()/3 + 1)*50) + 150);
		dimensions.getContentPane().setLayout(null);
		JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 750, ((possibleDimensions.size()/3 + 1)*50) + 150);
		JPanel checklistPanel = new JPanel();
		checklistPanel.setVisible(true);
		checklistPanel.setLayout(null);
		checklistPanel.setBounds(0, 0, 750, ((possibleDimensions.size()/3 + 1)*50) + 150);
		JCheckBox[] checkboxes = new JCheckBox[possibleDimensions.size()];
		for (int i=0; i < possibleDimensions.size(); i++){
			JCheckBox temp = new JCheckBox(possibleDimensions.get(i));
			temp.setBounds(((i%3)*200)+100, ((i/3)*50)+20, 200, 50);
			temp.setFont(new Font("Tahoma", Font.PLAIN, 18));
			checklistPanel.add(temp);
			checkboxes[i] = temp;
		}
		JButton btnSetDimensions = new JButton("Set Dimensions");
		btnSetDimensions.setBounds(166, ((possibleDimensions.size()/3 + 1)*50)+30, 150, 30);
		checklistPanel.add(btnSetDimensions);
		btnSetDimensions.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 userDimensions.clear();
	        	 for (int i=0; i < possibleDimensions.size(); i++){
	        		 if (checkboxes[i].isSelected()){
	        			 userDimensions.add(possibleDimensions.get(i));
	        		 }
	        	 }
	        	 dimensions.setVisible(false);
	        	 initializeMeasures(integerColumns);
	         }          
	      });
		JButton btnSelectAll = new JButton("Select All");
		btnSelectAll.setBounds(400, ((possibleDimensions.size()/3 + 1)*50)+30, 150, 30);
		checklistPanel.add(btnSelectAll);
		btnSelectAll.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 for (int i=0; i < checkboxes.length; i++){
	        		 checkboxes[i].setSelected(true);
	        	 }
	         }          
	      });
		mainPanel.add(checklistPanel);
		dimensions.add(mainPanel);
		dimensions.setVisible(true);
	}
	
	private void initializeMeasures(java.util.List<String> list) {
		measures = new JFrame("Measures");
		list.remove(column);
		measures.setBounds(200, 100, 750, ((list.size()/3 + 1)*50) + 150);
		measures.getContentPane().setLayout(null);
		JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 750, ((list.size()/3 + 1)*50) + 150);
		JPanel checklistPanel = new JPanel();
		checklistPanel.setVisible(true);
		checklistPanel.setLayout(null);
		checklistPanel.setBounds(0, 0, 750, ((list.size()/3 + 1)*50) + 150);
		JCheckBox[] checkboxes = new JCheckBox[list.size()];
		for (int i=0; i < list.size(); i++){
			JCheckBox temp = new JCheckBox(list.get(i));
			temp.setBounds(((i%3)*200)+100, ((i/3)*50)+20, 200, 50);
			temp.setFont(new Font("Tahoma", Font.PLAIN, 18));
	        checklistPanel.add(temp);
	        checkboxes[i] = temp;
		}
		JButton btnSetDimensions = new JButton("Set Measures");
		btnSetDimensions.setBounds(166, ((list.size()/3 + 1)*50)+30, 150, 30);
		checklistPanel.add(btnSetDimensions);
		btnSetDimensions.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 userMeasures.clear();
	        	 for (int i=0; i < list.size(); i++){
	        		 if (checkboxes[i].isSelected()){
	        			 userMeasures.add(list.get(i));
	        		 }
	        	 }
	        	 measures.setVisible(false);
	        	 System.out.println(userDimensions);
	        	 System.out.println(userMeasures);
	        	 List<String> binnedDimensions = new ArrayList<String>(userDimensions);
	        	 binnedDimensions.retainAll(integerColumns);
	        	 System.out.println(binnedDimensions);
	        	 if (binValue == 0) {
	        		 binnedDimensions.clear();
	        	 }
	        	 graph.startSeeDB(userDimensions, userMeasures, query, dbsettings, aggregrate,
	        			 binnedDimensions, binValue, normalise, table, weighted);
	        	 binValue = 0;
	         }          
	      });
		JButton btnSelectAll = new JButton("Select All");
		btnSelectAll.setBounds(400, ((list.size()/3 + 1)*50)+30, 150, 30);
		checklistPanel.add(btnSelectAll);
		btnSelectAll.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 for (int i=0; i < checkboxes.length; i++){
	        		 checkboxes[i].setSelected(true);
	        	 }
	         }          
	      });
		mainPanel.add(checklistPanel);
		measures.add(mainPanel);
		measures.setVisible(true);
	}
}
