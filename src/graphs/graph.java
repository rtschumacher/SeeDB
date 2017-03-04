package graphs;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities;

import com.google.common.collect.Lists;

import api.SeeDB;
import common.InputQuery;
import common.InputTablesMetadata;
import common.QueryParser;
import common.Utils;
import db_wrappers.DBConnection;
import settings.DBSettings;
import settings.ExperimentalSettings;
import settings.ExperimentalSettings.ComparisonType;
import settings.ExperimentalSettings.DifferenceOperators;
import views.AggregateGroupByView;
import views.View; 

public class graph extends ApplicationFrame
{
   public graph( String applicationTitle , String chartTitle , String[] columns, Double[] results)
   {
      super( applicationTitle );        
      JFreeChart barChart = ChartFactory.createBarChart(
         chartTitle,           
         "Column",            
         "Result",            
         createDataset(columns, results),          
         PlotOrientation.VERTICAL,           
         false, true, false);
         
      ChartPanel chartPanel = new ChartPanel( barChart );        
      chartPanel.setPreferredSize(new java.awt.Dimension( 1800 , 1200 ) );        
      setContentPane( chartPanel ); 
   }
   private CategoryDataset createDataset(String[] columns, Double[] results)
   {
      
      final DefaultCategoryDataset dataset = 
      new DefaultCategoryDataset( );  

      for (int i=0; i<results.length; i++){
    	  dataset.addValue( results[i] , "" , columns[i]);
      }
      
      return dataset; 
   }
   
   public static List<String> getColumns(){
	    String defaultQuery1 = "SELECT * FROM bank WHERE age=35";
		List<String> allColumns = new ArrayList<String>();
		ExperimentalSettings settings = new ExperimentalSettings();
		settings.differenceOperators = Lists.newArrayList();
		settings.differenceOperators.add(DifferenceOperators.AGGREGATE);
		settings.comparisonType = ComparisonType.ONE_DATASET_FULL;
		DBConnection connection = new DBConnection();
		if (!connection.hasConnection()) {
			System.out.println("DB connection not specified, using default connection params");
			// populate connection from settings
			if (!connection.connectToDatabase(DBSettings.getDefault())) {
				System.out.println("Cannot connect to DB with default settings");
				return null;
			}
		}
		InputQuery[] inputQueries = new InputQuery[]{null, null};
		try {
			inputQueries[0] = QueryParser.parse(defaultQuery1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = connection.getTableColumns(inputQueries[0].tables.get(0));
		try {
			while (rs.next()) {
			    String columnName = rs.getString("COLUMN_NAME");
			    allColumns.add(columnName);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i=0; i < allColumns.size(); i++){
			System.out.println(allColumns.get(i));
		}
		connection.close();
		return allColumns;
   }
   
   public static List<String> getIntegerColumns(){
	    String defaultQuery1 = "SELECT * FROM bank WHERE age=35";
		List<String> allColumns = new ArrayList<String>();
		ExperimentalSettings settings = new ExperimentalSettings();
		settings.differenceOperators = Lists.newArrayList();
		settings.differenceOperators.add(DifferenceOperators.AGGREGATE);
		settings.comparisonType = ComparisonType.ONE_DATASET_FULL;
		DBConnection connection = new DBConnection();
		if (!connection.hasConnection()) {
			System.out.println("DB connection not specified, using default connection params");
			// populate connection from settings
			if (!connection.connectToDatabase(DBSettings.getDefault())) {
				System.out.println("Cannot connect to DB with default settings");
				return null;
			}
		}
		InputQuery[] inputQueries = new InputQuery[]{null, null};
		try {
			inputQueries[0] = QueryParser.parse(defaultQuery1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		allColumns =  connection.getIntegerColumns(inputQueries[0].tables.get(0));
		connection.close();
		return allColumns;
  }
   
   public static void startSeeDB(List<String> dimensions, List<String> measures, String query)
   {	
	    //String defaultQuery1 = "SELECT * FROM bank WHERE age=35"
	   	String defaultQuery1 = query;
		SeeDB seedb = new SeeDB();
		List<String> allColumns = new ArrayList<String>();
		ExperimentalSettings settings = new ExperimentalSettings();
		settings.differenceOperators = Lists.newArrayList();
		settings.differenceOperators.add(DifferenceOperators.AGGREGATE);
		settings.comparisonType = ComparisonType.ONE_DATASET_FULL;
		DBConnection connection = new DBConnection();
		if (!connection.hasConnection()) {
			System.out.println("DB connection not specified, using default connection params");
			// populate connection from settings
			if (!connection.connectToDatabase(DBSettings.getDefault())) {
				System.out.println("Cannot connect to DB with default settings");
				return;
			}
		}
		InputQuery[] inputQueries = new InputQuery[]{null, null};
		try {
			inputQueries[0] = QueryParser.parse(defaultQuery1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = connection.getTableColumns(inputQueries[0].tables.get(0));
		try {
			while (rs.next()) {
			    String columnName = rs.getString("COLUMN_NAME");
			    allColumns.add(columnName);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<View> result;
		try {
			System.out.println("Inside 4-1");
			seedb.initialize(defaultQuery1, null, settings);
			System.out.println("Inside 4-2");
			result = seedb.computeDifference(dimensions, measures);
			System.out.println("Inside 4-3");
			Utils.printList(result);
			System.out.println("Inside 4-4");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	    String[] columns = new String[result.size()];
	    Double[] results = new Double[result.size()];
	    for (int i = 0; i < result.size(); i++){
	    	columns[i] = (String) ((AggregateGroupByView) result.get(i)).getId();
	    	columns[i] = columns[i].replaceAll("dim_", "");
	    	columns[i] = columns[i].replaceAll("measure_", "");
	    }
	    for (int i = 0; i < result.size(); i++){
	    	results[i] = (Double) result.get(i).getUtility(settings.distanceMetric, settings.normalizeDistributions);
	    }
		graph chart = new graph("SeeDB Results", "SeeDB Results", columns, results);
	    chart.pack( );        
	    RefineryUtilities.centerFrameOnScreen( chart );        
	    chart.setVisible( true ); 
   }
}