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
	private String query = null;
	

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
		frame.setBounds(100, 100, 1161, 711);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 461, 1139, 194);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		panel.setLayout(null);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		scrollBar.setBounds(0, 174, 1139, 20);
		panel.add(scrollBar);
		
		JLabel lblSeedbReccommendations = new JLabel("SeeDB Recommendations");
		lblSeedbReccommendations.setBounds(492, 16, 180, 20);
		panel.add(lblSeedbReccommendations);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(601, 0, 548, 462);
		frame.getContentPane().add(panel_1);
		
		JLabel lblVisualisation = new JLabel("Visualisation");
		panel_1.add(lblVisualisation);
		panel_2.setBounds(0, 284, 603, 177);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblVisualisationBuilder = new JLabel("Visualisation Builder");
		lblVisualisationBuilder.setBounds(230, 5, 142, 20);
		panel_2.add(lblVisualisationBuilder);
		
		JLabel lblXAxis = new JLabel("X Axis");
		lblXAxis.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblXAxis.setBounds(82, 41, 69, 20);
		panel_2.add(lblXAxis);
		
		JList list = new JList();
		list.setBounds(161, 41, 185, 20);
		panel_2.add(list);
		
		JLabel lblYAxis = new JLabel("Y Axis");
		lblYAxis.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblYAxis.setBounds(82, 81, 69, 20);
		panel_2.add(lblYAxis);
		
		JList list_1 = new JList();
		list_1.setBounds(161, 81, 185, 20);
		panel_2.add(list_1);
		
		JButton btnBuildVisualisation = new JButton("Build Visualisation");
		btnBuildVisualisation.setBounds(392, 75, 161, 29);
		panel_2.add(btnBuildVisualisation);
		
		JLabel lblAgre = new JLabel("Aggregate");
		lblAgre.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAgre.setBounds(58, 121, 93, 20);
		panel_2.add(lblAgre);
		
		JList list_2 = new JList();
		list_2.setBounds(161, 121, 185, 20);
		panel_2.add(list_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 0, 603, 285);
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblQuery = new JLabel("Query");
		lblQuery.setBounds(280, 5, 43, 20);
		panel_3.add(lblQuery);
		
		JLabel lblDataset = new JLabel("Dataset");
		lblDataset.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDataset.setBounds(59, 51, 69, 20);
		panel_3.add(lblDataset);
		
		JTextField list_3 = new JTextField();
		list_3.setBounds(138, 51, 185, 20);
		panel_3.add(list_3);
		
		JButton btnAddDataset = new JButton("Add Dataset");
		btnAddDataset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				query = list_3.getText();
				System.out.println(query);
			}
		});
		btnAddDataset.setBounds(366, 48, 161, 29);
		panel_3.add(btnAddDataset);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(15, 91, 573, 137);
		panel_3.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setBounds(269, 5, 35, 20);
		panel_4.add(lblFilter);
		
		JLabel lblAttribute = new JLabel("Attribute");
		lblAttribute.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAttribute.setBounds(40, 41, 69, 20);
		panel_4.add(lblAttribute);
		
		JList list_4 = new JList();
		list_4.setBounds(119, 41, 185, 20);
		panel_4.add(list_4);
		
		JLabel lblOperator = new JLabel("Operator");
		lblOperator.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblOperator.setBounds(40, 77, 69, 20);
		panel_4.add(lblOperator);
		
		JList list_5 = new JList();
		list_5.setBounds(119, 77, 185, 20);
		panel_4.add(list_5);
		
		JLabel lblValue = new JLabel("Value");
		lblValue.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblValue.setBounds(40, 113, 69, 20);
		panel_4.add(lblValue);
		
		JList list_6 = new JList();
		list_6.setBounds(119, 113, 185, 20);
		panel_4.add(list_6);
		
		JButton btnAddDatabase = new JButton("Edit Database");
		btnAddDatabase.setBounds(370, 60, 161, 29);
		panel_4.add(btnAddDatabase);
		btnAddDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				initializeConnection(1);
			}
		});
		
		JButton btnGetRecommendations = new JButton("Get Results");
		btnGetRecommendations.setBounds(396, 244, 192, 29);
		panel_3.add(btnGetRecommendations);
		btnGetRecommendations.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	if (dbsettings == null) {
	        		JOptionPane.showMessageDialog(frame, "Please Specify A Database");
		     	} else if (query == null){
		     		JOptionPane.showMessageDialog(frame, "Please Specify A Dataset");
		     	} else {
		     		initializeDimensions(graph.getColumns(dbsettings, query));
		     	}
	         }
	      });
	}
	
	private void initializeConnection(int indicator) {
		dbconnection = new JFrame("Database Connection");
		dbconnection.setBounds(200, 100, 500, 400);
		dbconnection.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		textHost.setBounds(200, 50, 200, 20);
		hostAddress.add(textHost);
		     
        JLabel lblType = new JLabel("Database Type");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblType.setBounds(50, 100, 200, 20);
		hostAddress.add(lblType);
		
		JTextField textType = new JTextField();
		textType.setBounds(200, 100, 200, 20);
		hostAddress.add(textType);
		      
        JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsername.setBounds(50, 150, 200, 20);
		hostAddress.add(lblUsername);
		
		JTextField textUsername = new JTextField();
		textUsername.setBounds(200, 150, 200, 20);
		hostAddress.add(textUsername);
        
        JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPassword.setBounds(50, 200, 200, 20);
		hostAddress.add(lblPassword);
		
		JPasswordField textPassword = new JPasswordField();
		textPassword.setEchoChar('*');
		textPassword.setBounds(200, 200, 200, 20);
		hostAddress.add(textPassword);
		
		JButton btnAddDatabaseSettings = new JButton("Set Database");
		btnAddDatabaseSettings.setBounds(75, 275, 150, 30);
		hostAddress.add(btnAddDatabaseSettings);
		btnAddDatabaseSettings.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	connection = new DBConnection();
	        	DBSettings tempdbsettings = new DBSettings(textHost.getText(), textType.getText(),
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
		dimensions.setBounds(200, 100, 750, ((list.size()/3 + 1)*50) + 150);
		dimensions.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dimensions.getContentPane().setLayout(null);
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
	        checklistPanel.add(temp);
	        checkboxes[i] = temp;
		}
		JButton btnSetDimensions = new JButton("Set Dimensions");
		btnSetDimensions.setBounds(166, ((list.size()/3 + 1)*50)+30, 150, 30);
		checklistPanel.add(btnSetDimensions);
		btnSetDimensions.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 userDimensions.clear();
	        	 for (int i=0; i < list.size(); i++){
	        		 if (checkboxes[i].isSelected()){
	        			 userDimensions.add(list.get(i));
	        		 }
	        	 }
	        	 dimensions.setVisible(false);
	        	 initializeMeasures(graph.getIntegerColumns(dbsettings, query));
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
		dimensions.add(mainPanel);
		dimensions.setVisible(true);
	}
	
	private void initializeMeasures(java.util.List<String> list) {
		measures = new JFrame("Measures");
		measures.setBounds(200, 100, 750, ((list.size()/3 + 1)*50) + 150);
		measures.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	        	 graph.startSeeDB(userDimensions, userMeasures, query, dbsettings);
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
