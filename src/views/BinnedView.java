package views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BinnedView{
	private List<AggregateGroupByView> views = new ArrayList<AggregateGroupByView>();
	private HashMap<String, ArrayList<Double>> sums = new HashMap<String, ArrayList<Double>>();
	private HashMap<String, ArrayList<Double>> counts = new HashMap<String, ArrayList<Double>>();
	private HashMap<String, ArrayList<Double>> avgs = new HashMap<String, ArrayList<Double>>();
	private Double distance;
	private List<Double> querySum = new ArrayList<Double>();
	private List<Double> referenceSum = new ArrayList<Double>();
	private List<Double> queryAvg = new ArrayList<Double>();
	private List<Double> referenceAvg = new ArrayList<Double>();
	private List<Double> queryCount = new ArrayList<Double>();
	private List<Double> referenceCount = new ArrayList<Double>();
	
	public BinnedView(List<AggregateGroupByView> binnedTemp, String aggregate){
		this.views = binnedTemp;
		if (aggregate.equals("SUM")){
			populateSum();
		} else if (aggregate.equals("AVG")){
			populateAvg();
		} else if (aggregate.equals("COUNT")){
			populateCount();
		}
		populateDistance(aggregate);
	}
	
	public void populateSum() {
		int i = 0;
		for (View view : views){
			 sums = (HashMap<String, ArrayList<Double>>) ((AggregateGroupByView) view).getSum(true);
			 querySum.add(i, (double) 0);
			 referenceSum.add(i, (double) 0);
			 double queryTemp = querySum.get(i);
			 double referenceTemp = referenceSum.get(i);
			 for (ArrayList<Double> value : sums.values()) {
				 queryTemp = querySum.get(i);
				 referenceTemp = referenceSum.get(i);
				 querySum.add(i, queryTemp + value.get(0));
				 referenceSum.add(i, referenceTemp + value.get(1));
			 }
			 queryTemp = querySum.get(i);
			 referenceTemp = referenceSum.get(i);
			 querySum.add(i, queryTemp/sums.values().size());
			 referenceSum.add(i, referenceTemp/sums.values().size());
			 i++;
		}
	}
	
	public void populateAvg() {
		int i = 0;
		for (View view : views){
			 avgs = (HashMap<String, ArrayList<Double>>) ((AggregateGroupByView) view).getAvg();
			 queryAvg.add(i, (double) 0);
			 referenceAvg.add(i, (double) 0);
			 double queryTemp = queryAvg.get(i);
			 double referenceTemp = referenceAvg.get(i);
			 for (ArrayList<Double> value : avgs.values()) {
				 queryTemp = queryAvg.get(i);
				 referenceTemp = referenceAvg.get(i);
				 queryAvg.add(i, queryTemp + value.get(0));
				 referenceAvg.add(i, referenceTemp + value.get(1));
			 }
			 queryTemp = queryAvg.get(i);
			 referenceTemp = referenceAvg.get(i);
			 queryAvg.add(i, queryTemp/avgs.values().size());
			 referenceAvg.add(i, referenceTemp/avgs.values().size());
			 i++;
		}
	}
	
	public void populateCount() {
		int i = 0;
		for (View view : views){
			 counts = (HashMap<String, ArrayList<Double>>) ((AggregateGroupByView) view).getCount();
			 queryCount.add(i, (double) 0);
			 referenceCount.add(i, (double) 0);
			 double queryTemp = queryCount.get(i);
			 double referenceTemp = referenceCount.get(i);
			 for (ArrayList<Double> value : counts.values()) {
				 queryTemp = queryCount.get(i);
				 referenceTemp = referenceCount.get(i);
				 queryCount.add(i, queryTemp + value.get(0));
				 referenceCount.add(i, referenceTemp + value.get(1));
			 }
			 queryTemp = queryCount.get(i);
			 referenceTemp = referenceCount.get(i);
			 queryCount.add(i, queryTemp/counts.values().size());
			 referenceCount.add(i, referenceTemp/counts.values().size());
			 i++;
		}
	}
	
	public void populateDistance(String aggregate){
		distance = (double) 0;
		if (aggregate.equals("SUM")){
			for (int i = 0; i < querySum.size(); i++) {
				distance += Math.pow(querySum.get(i) - 
										referenceSum.get(i), 
									2);
			}
			distance = Math.sqrt(distance);
		} else if (aggregate.equals("AVG")){
			for (int i = 0; i < queryAvg.size(); i++) {
				distance += Math.pow(queryAvg.get(i) - 
										referenceAvg.get(i), 
									2);
			}
			distance = Math.sqrt(distance);
		} else if (aggregate.equals("COUNT")){
			for (int i = 0; i < queryCount.size(); i++) {
				distance += Math.pow(queryCount.get(i) - 
										referenceCount.get(i), 
									2);
			}
			distance = Math.sqrt(distance);
		}
	}
	
	
}