package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import settings.DBSettings;
import views.AggregateGroupByView;
import views.View;

public class seeDBPlot extends JFrame
{
	public seeDBPlot(String category, String dimension, String measure, List<View> result, 
			String where, String aggregate)
	{
		super( "Dataset Results" );        
		JFreeChart barChart = ChartFactory.createBarChart(
				dimension + " vs " + aggregate + "(" + measure + ")",           
				"Column",            
				"Result",            
				createDataset(result, where, category, aggregate),
				PlotOrientation.VERTICAL,           
				true, true, false);
      
		ChartPanel chartPanel = new ChartPanel( barChart );
		CategoryPlot p = barChart.getCategoryPlot();
		CategoryAxis a = p.getDomainAxis();
		a.setCategoryMargin(0);
		BarRenderer r = (BarRenderer) p.getRenderer();
		r.setItemMargin(0);
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
	
	 private CategoryDataset createDataset(List<View> result, String where, String category, String aggregate) {
		 HashMap<String, ArrayList<Double>> values = new HashMap<String, ArrayList<Double>>();
		 final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		 
		 for (int i=0; i < result.size(); i++){
			 AggregateGroupByView temp = (AggregateGroupByView) result.get(i);
			 String id = temp.getId();
			 if (category.equals(id)){
				 if (aggregate.equals("SUM")){
					 values = temp.getSum();
				 } else if (aggregate.equals("COUNT")){
					 values = temp.getCount();
				 } else if (aggregate.equals("AVG")){
					 values = temp.getAvg();
				 }
				 break;
			 }
		 }
		 
		 for (String key : values.keySet()){
			 System.out.println(key + values.get(key));
			 for (int i = 0; i < 2; i++){
				 if (i == 0){
					 dataset.addValue(values.get(key).get(i), "Query Dataset (" + where + ")", key);
				 } else if (i == 1) {
					 dataset.addValue(values.get(key).get(i), "Reference Dataset", key);
				 }
			 }
		 }
		 
		 return dataset;
	   }
}