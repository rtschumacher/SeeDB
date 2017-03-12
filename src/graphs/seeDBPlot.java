package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import settings.DBSettings;
import views.AggregateGroupByView;
import views.View;

public class seeDBPlot extends ApplicationFrame
{
	public seeDBPlot(String category, String dimension, String measure, List<View> result, String where)
	{
		super( "Dataset Results" );        
		JFreeChart barChart = ChartFactory.createBarChart(
				dimension + " vs SUM(" + measure + ")",           
				"Column",            
				"Result",            
				createDataset(result, where, category),
				PlotOrientation.VERTICAL,           
				true, true, false);
      
		ChartPanel chartPanel = new ChartPanel( barChart );
		chartPanel.addChartMouseListener(new ChartMouseListener() {

 	    public void chartMouseClicked(ChartMouseEvent e) {
 	    	try {
 	    		CategoryItemEntity entity = (CategoryItemEntity) e.getEntity();
 	    		String category = entity.getCategory().toString();
 	    		System.out.println(category);
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
	
	 private CategoryDataset createDataset(List<View> result, String where, String category) {
		 String inverseWhere = where.replaceAll("=", "!=");
		 HashMap<String, ArrayList<Double>> values = new HashMap<String, ArrayList<Double>>();
		 final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		 System.out.println(where);
		 System.out.println(inverseWhere);
		 
		 for (int i=0; i < result.size(); i++){
			 AggregateGroupByView temp = (AggregateGroupByView) result.get(i);
			 String id = temp.getId();
			 if (category.equals(id)){
				 values = temp.getSum();
				 break;
			 }
		 }
		 
		 for (String key : values.keySet()){
			 System.out.println(key + values.get(key));
			 for (int i = 0; i < 2; i++){
				 if (i == 0){
					 dataset.addValue(values.get(key).get(i), where, key);
				 } else if (i == 1) {
					 dataset.addValue(values.get(key).get(i), inverseWhere, key);
				 }
			 }
		 }
		 
		 return dataset;
	   }
}