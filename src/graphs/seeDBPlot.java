package graphs;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
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
			String where, String aggregate, List<String> binnedDimensions, Boolean normalise)
	{
		super( "Dataset Results" );        
		JFreeChart barChart = ChartFactory.createBarChart(
				dimension + " vs " + aggregate + "(" + measure + ")",           
				"Column",            
				"Result",            
				createDataset(result, where, category, aggregate, binnedDimensions, normalise),
				PlotOrientation.VERTICAL,           
				true, true, false);
      
		ChartPanel chartPanel = new ChartPanel( barChart );
		CategoryPlot p = barChart.getCategoryPlot();
		CategoryAxis a = p.getDomainAxis();
		a.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		if (normalise){
			a.setCategoryMargin(0);
		}
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
		if (p.getCategories().size() > 20){
			System.out.println("HERE");
			JSlider slider = new JSlider(0, result.size() % 20);
			slider.setMajorTickSpacing(1);
			slider.setMinorTickSpacing(1);
			slider.setSnapToTicks(true);
			slider.setPaintTicks(true);
			slider.setPaintTrack(true);
			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e){
					
					//barChart.getCategoryPlot().setDataset(sliderDataset(null, slider.getValue()));
				}
			});
			Box bot = new Box(BoxLayout.X_AXIS);
			bot.add(slider);
			this.add(bot, BorderLayout.SOUTH);
			this.pack();
		}
		chartPanel.setPreferredSize(new java.awt.Dimension( 1800 , 1200 ) );        
		//setContentPane( chartPanel );
		this.add(chartPanel);
		this.pack();
	}
	
	private CategoryDataset sliderDataset(CategoryDataset ds, int sliderValue){
		return ds;
	}
	
	 private CategoryDataset createDataset(List<View> result, String where, 
			 String category, String aggregate, List<String> binnedDimensions, Boolean normalise) {
		 HashMap<String, ArrayList<Double>> values = new HashMap<String, ArrayList<Double>>();
		 final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		 
		 String attribute = null;
		 
		 for (int i=0; i < result.size(); i++){
			 AggregateGroupByView temp = (AggregateGroupByView) result.get(i);
			 String id = temp.getId();
			 attribute = temp.groupByAttribute;
			 if (category.equals(id)){
				 if (aggregate.equals("SUM")){
					 values = temp.getSum(normalise);
				 } else if (aggregate.equals("COUNT")){
					 values = temp.getCount(normalise);
				 } else if (aggregate.equals("AVG")){
					 values = temp.getAvg(normalise);
				 }
				 break;
			 }
		 }
		 
		 List<String> sort = new ArrayList();
		 
		 for (String key : values.keySet()){
			 sort.add(key);
		 }
		 
		 if (binnedDimensions != null){
			 if (binnedDimensions.contains(attribute)){
				 Collections.sort(sort, new Comparator<String>() {
				     public int compare(String str1, String str2){
				    	 String[] str1Parts = str1.split(" ");
				    	 String[] str2Parts = str2.split(" ");
				    	 Double db1 = Double.parseDouble(str1Parts[0]);
				    	 Double db2 = Double.parseDouble(str2Parts[0]);
				         return db1.compareTo(db2);
				     }
				 });
			 }
		 }
		 
		 for (String key : sort){
			 System.out.println(key + values.get(key));
			 for (int i = 0; i < 2; i++){
				 if (i == 0){
					 if (normalise){
						 dataset.addValue(values.get(key).get(i), "Query Dataset (" + where + ")", key);
					 } else {
						 dataset.addValue(values.get(key).get(i), key, "Query Dataset (" + where + ")");
					 }
				 } else if (i == 1) {
					 if (normalise){
						 dataset.addValue(values.get(key).get(i), "Reference Dataset", key);
					 } else {
						 dataset.addValue(values.get(key).get(i), key, "Reference Dataset");
					 }
				 }
			 }
		 }
		 
		 return dataset;
	   }
}