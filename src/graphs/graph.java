package graphs;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities;

import com.google.common.collect.Lists;

import api.SeeDB;
import common.InputQuery;
import common.QueryParser;
import common.Utils;
import db_wrappers.DBConnection;
import settings.DBSettings;
import settings.ExperimentalSettings;
import settings.ExperimentalSettings.ComparisonType;
import settings.ExperimentalSettings.DifferenceOperators;
import views.AggregateGroupByView;
import views.View; 

public class graph extends JFrame
{
	
	private int index = 0;
   public graph( String applicationTitle , String chartTitle , String[] columns, Double[] results,
		   List<View> result, String where, String aggregrate, List<String> binnedDimensions, Boolean normalise)
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
      ArrayList<seeDBPlot> datasets = new ArrayList<seeDBPlot>();
      chartPanel.addChartMouseListener(new ChartMouseListener() {

    	    public void chartMouseClicked(ChartMouseEvent e) {
    	    	try {
    	    		CategoryItemEntity entity = (CategoryItemEntity) e.getEntity();
    	    		String category = entity.getCategory().toString();
    	    		String[] categories = category.split("__");
    	    		if (aggregrate.equals("ALL")){
    	    			datasets.add(index, new seeDBPlot(categories[0] + "__" + categories[1], categories[0], categories[1], result, 
        	    				where, categories[2], binnedDimensions, normalise));
    	    		} else {
    	    			datasets.add(index, new seeDBPlot(category, categories[0], categories[1], result, 
    	    				where, aggregrate, binnedDimensions, normalise));
    	    		}
    	    		datasets.get(index).pack( );        
    	    	    RefineryUtilities.centerFrameOnScreen(datasets.get(index));
    	    	    datasets.get(index).setVisible( true ); 
    	    	    index++;
    	    	} catch (Exception e1) {
    	    		
    	    	}
    	    }

			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

    	});
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
   
   public static List<String> getColumns(DBSettings dbsettings, String table){
	    //String defaultQuery1 = "SELECT * FROM bank WHERE age=35 AND 1=0";
		List<String> allColumns = new ArrayList<String>();
		DBConnection connection = new DBConnection();
		if (!connection.hasConnection()) {
			System.out.println("DB connection not specified, using default connection params");
			// populate connection from settings
			if (!connection.connectToDatabase(dbsettings)) {
				System.out.println("Cannot connect to DB with default settings");
				return null;
			}
		}
		//InputQuery[] inputQueries = new InputQuery[]{null, null};
		//try {
		//	inputQueries[0] = QueryParser.parse(defaultQuery1);
		//} catch (Exception e1) {
			// TODO Auto-generated catch block
		//	e1.printStackTrace();
		//}
		//ResultSet rs = connection.getTableColumns(inputQueries[0].tables.get(0));
		ResultSet rs = connection.getTableColumns(table);
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
   
   public static List<String> getIntegerColumns(DBSettings dbsettings, String defaultQuery1){
	    //String defaultQuery1 = "SELECT * FROM bank WHERE age=35 AND 1=0";
		List<String> allColumns = new ArrayList<String>();
		DBConnection connection = new DBConnection();
		if (!connection.hasConnection()) {
			System.out.println("DB connection not specified, using default connection params");
			// populate connection from settings
			if (!connection.connectToDatabase(dbsettings)) {
				System.out.println("Cannot connect to DB with default settings");
				return null;
			}
		}
		//InputQuery[] inputQueries = new InputQuery[]{null, null};
		//try {
		//	inputQueries[0] = QueryParser.parse(defaultQuery1);
		//} catch (Exception e1) {
		//	// TODO Auto-generated catch block
		//	e1.printStackTrace();
		//}
		//allColumns = connection.getIntegerColumns(inputQueries[0].tables.get(0));
		allColumns = connection.getIntegerColumns(defaultQuery1);
		connection.close();
		return allColumns;
  }
   
   private static List<Object> sortResult(String[] columns, Double[] result){
	   List <String> tmpColumns = new ArrayList<String>(Arrays.asList(columns));
	   List <Double> tmpResults = new ArrayList<Double>(Arrays.asList(result));
	   columns = new String[tmpColumns.size()];
	   result = new Double[tmpResults.size()];
	   List<Object> ret = new ArrayList<Object>();
	   Double max;
	   String maxString;
	   int i = 0;
	   while (!tmpResults.isEmpty()){
		   max = Collections.max(tmpResults);
		   maxString = tmpColumns.get(tmpResults.indexOf(max));
		   result[i] = max;
		   columns[i] = maxString;
		   tmpResults.remove(max);
		   tmpColumns.remove(maxString);
		   i++;
	   }
	   ret.add(columns);
	   ret.add(result);
	   return ret;
   }
   
   public static void startSeeDB(List<String> dimensions, List<String> measures, String query,
		   DBSettings dbsettings, String aggregate, List<String> binnedDimensions,
		   Integer binValue, Boolean normalise)
   {	
	   	String defaultQuery1 = query;
		SeeDB seedb = new SeeDB();
		List<String> allColumns = new ArrayList<String>();
		ExperimentalSettings settings = new ExperimentalSettings();
		settings.differenceOperators = Lists.newArrayList();
		settings.differenceOperators.add(DifferenceOperators.AGGREGATE);
		settings.comparisonType = ComparisonType.ONE_DATASET_FULL;
		if (normalise == false){
			settings.normalizeDistributions = false;
		}
		DBConnection connection = new DBConnection();
		if (!connection.hasConnection()) {
			System.out.println("DB connection not specified, using default connection params");
			// populate connection from settings
			if (!connection.connectToDatabase(dbsettings)) {
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
		String where = inputQueries[0].whereClause;
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
			result = seedb.computeDifference(dimensions, measures, aggregate, binnedDimensions, binValue);
			System.out.println("Inside 4-3");
			//Utils.printList(result);
			System.out.println("Inside 4-4");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String[] columns = new String[result.size()];
		Double[] results = new Double[result.size()];
	    if (aggregate.equals("ALL")) {
	    	columns = new String[result.size()*3];
			results = new Double[result.size()*3];
	    	for (int i = 0; i < result.size(); i++){
	    		((AggregateGroupByView) result.get(i)).setFunction("SUM");
	    		columns[3*i] = (String) ((AggregateGroupByView) result.get(i)).getId() + "__SUM";
	    		results[3*i] = (Double) result.get(i).getUtility(settings.distanceMetric, settings.normalizeDistributions);
	    		((AggregateGroupByView) result.get(i)).setFunction("COUNT");
	    		columns[(3*i)+1] = (String) ((AggregateGroupByView) result.get(i)).getId() + "__COUNT";
	    		results[(3*i)+1] = (Double) result.get(i).getUtility(settings.distanceMetric, settings.normalizeDistributions);
	    		((AggregateGroupByView) result.get(i)).setFunction("AVG");
	    		columns[(3*i)+2] = (String) ((AggregateGroupByView) result.get(i)).getId() + "__AVG";
	    		results[(3*i)+2] = (Double) result.get(i).getUtility(settings.distanceMetric, settings.normalizeDistributions);
	    	}
	    } else {
	    	for (int i = 0; i < result.size(); i++){
	    		columns[i] = (String) ((AggregateGroupByView) result.get(i)).getId();
	    	}
	    	for (int i = 0; i < result.size(); i++){
	    		results[i] = (Double) result.get(i).getUtility(settings.distanceMetric, settings.normalizeDistributions);
	    	}
	    }
	    List<Object> sort = sortResult(columns, results);
	    columns = (String[]) sort.get(0);
	    results = (Double[]) sort.get(1);
		graph chart = new graph("SeeDB Results", "SeeDB Results", columns, results, result, where, aggregate, binnedDimensions, normalise);
	    chart.pack( );        
	    RefineryUtilities.centerFrameOnScreen( chart );        
	    chart.setVisible( true ); 
   }
}