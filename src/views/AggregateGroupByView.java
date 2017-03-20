package views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import settings.ExperimentalSettings.DifferenceOperators;
import settings.ExperimentalSettings.DistanceMetric;
import utils.Constants;
import utils.Constants.AggregateFunctions;
import utils.UtilityMetrics;
import views.AggregateValuesWrapper.AggregateValues;
import common.DifferenceQuery;

public class AggregateGroupByView extends AggregateView implements Cloneable {

	private boolean populatedAvg = false;
	private AggregateFunctions func = AggregateFunctions.ALL;
	public double utility = -1;
	
	public double getUtility() {
		return utility;
	}
	public AggregateGroupByView(DifferenceQuery dq) {
		super(dq);
	}
	
	public AggregateGroupByView(String groupByAttribute, String aggregateAttribute) {
		super(groupByAttribute, aggregateAttribute);
	}

	@Override
	public DifferenceOperators getOperator() {
		return DifferenceOperators.AGGREGATE;
	}

	public String getGroupByAttributes() {
		return this.groupByAttribute;
	}
	
	public String getAggregateAttribute() {
		return this.aggregateAttribute;
	}
	
	public void populateAvg() {
		if (populatedAvg) {
			return;
		}
		for (String key : this.aggregateValues.keySet()) {
			AggregateValuesWrapper wrapper = aggregateValues.get(key);
			for (int i = 0; i < 2; i++) {
				if (wrapper.datasetValues[i].count != 0) {
					wrapper.datasetValues[i].average = wrapper.datasetValues[i].sum*1.0 / wrapper.datasetValues[i].count;
				}
			}
		}
		populatedAvg = true;
	}

	public HashMap<String, AggregateValuesWrapper> getResult() {
		populateAvg();
		normalize();
		System.out.println("getResult");
		return this.aggregateValues;
	}

	public String toString() {
		populateAvg();
		String ret = "";
		ret += "diff_type:aggregate_diff;";
		ret += "aggregateValues:" + this.aggregateAttribute + ";";
		ret += "groupByValues:" + this.groupByAttribute + ";";
		ret += "data:[[";
		for (String key: this.aggregateValues.keySet()) {
			ret += key + ":";
			for (int i = 0; i < 2; i++) {
				AggregateValues tmp = aggregateValues.get(key).datasetValues[i];
				//System.out.println("Count Normalised: " + tmp.countNormalized);
				ret += tmp.countNormalized + ";";
				//System.out.println("Sum Normalised: " + tmp.sum);
				ret += tmp.sumNormalized + ";";
			}
		}
		ret += "]]";
		return ret;
	}
	
	public HashMap<String, ArrayList<Double>> getSum(boolean normalise){
		if (normalise){
			normalize();
		}
		populateAvg();
		HashMap<String, ArrayList<Double>> ret = new HashMap<String, ArrayList<Double>>();
		for (String key: this.aggregateValues.keySet()) {
			ArrayList<Double> temp = new ArrayList<Double>();
			for (int i = 0; i < 2; i++) {
				AggregateValues tmp = aggregateValues.get(key).datasetValues[i];
				//System.out.println("Sum Normalised: " + tmp.sum);
				temp.add(i, tmp.sumNormalized);
			}
			ret.put(key, temp);
		}
		return ret;
	}
	
	public HashMap<String, ArrayList<Double>> getAvg(){
		populateAvg();
		HashMap<String, ArrayList<Double>> ret = new HashMap<String, ArrayList<Double>>();
		for (String key: this.aggregateValues.keySet()) {
			ArrayList<Double> temp = new ArrayList<Double>();
			for (int i = 0; i < 2; i++) {
				AggregateValues tmp = aggregateValues.get(key).datasetValues[i];
				//System.out.println("Sum Normalised: " + tmp.sum);
				temp.add(i, tmp.averageNormalized);
			}
			ret.put(key, temp);
		}
		return ret;
	}
	
	public HashMap<String, ArrayList<Double>> getCount(){
		populateAvg();
		HashMap<String, ArrayList<Double>> ret = new HashMap<String, ArrayList<Double>>();
		for (String key: this.aggregateValues.keySet()) {
			ArrayList<Double> temp = new ArrayList<Double>();
			for (int i = 0; i < 2; i++) {
				AggregateValues tmp = aggregateValues.get(key).datasetValues[i];
				//System.out.println("Sum Normalised: " + tmp.sum);
				temp.add(i, tmp.countNormalized);
			}
			ret.put(key, temp);
		}
		return ret;
	}	
	
	private AggregateFunctions getAggregateFunctionForUtility() {
		if (func == AggregateFunctions.ALL) return AggregateFunctions.ALL;
		return func;
	}

	@Override
	public double getUtility(DistanceMetric distanceMetric, boolean normalize) {
		if (normalize) {
			normalize();
		}
		populateAvg();
		switch (distanceMetric) {
		case EARTH_MOVER_DISTANCE:
			utility = UtilityMetrics.EarthMoverDistance(this.aggregateValues, getAggregateFunctionForUtility(), normalize);
			break;
		case EUCLIDEAN_DISTANCE:
			utility = UtilityMetrics.EuclideanDistance(this.aggregateValues, getAggregateFunctionForUtility(), normalize);
			break;
		case COSINE_DISTANCE:
			utility = UtilityMetrics.CosineDistance(this.aggregateValues, getAggregateFunctionForUtility(), normalize);
			break;
		case FIDELITY_DISTANCE:
			utility = UtilityMetrics.FidelityDistance(this.aggregateValues, getAggregateFunctionForUtility(), normalize);
			break;
		case CHI_SQUARED_DISTANCE:
			utility = UtilityMetrics.ChiSquaredDistance(this.aggregateValues, getAggregateFunctionForUtility(), normalize);
			break;
		case KULLBACK_LEIBLER_DISTANCE:
			utility = UtilityMetrics.EntropyDistance(this.aggregateValues, getAggregateFunctionForUtility(), normalize);
			break;
		}
		return utility;
	}
	
	@Override
	public double getUtility(DistanceMetric distanceMetric) {
		utility = getUtility(distanceMetric, true);
		return utility;
	}
	
	private void normalize() {
		double totalSum[] = {0, 0};
		double totalCount[] = {0, 0};
		double totalAverage[] = {0, 0};
		
		for (String key : this.aggregateValues.keySet()) {
			AggregateValuesWrapper wrapper = aggregateValues.get(key);
			for (int i = 0; i < 2; i++) {
				totalSum[i] += wrapper.datasetValues[i].sum;
				totalCount[i] += wrapper.datasetValues[i].count;
				totalAverage[i] += wrapper.datasetValues[i].average;
			}
		}
		for (String key : this.aggregateValues.keySet()) {
			AggregateValuesWrapper wrapper = aggregateValues.get(key);
			for (int i = 0; i < 2; i++) {
				if (totalSum[i] > 0) {
					wrapper.datasetValues[i].sumNormalized = wrapper.datasetValues[i].sum / totalSum[i];
				}
				if (totalCount[i] > 0) {
					wrapper.datasetValues[i].countNormalized = wrapper.datasetValues[i].count / totalCount[i];
				}
				if (totalAverage[i] > 0) {
					wrapper.datasetValues[i].averageNormalized = wrapper.datasetValues[i].average / totalAverage[i];
				}
			}
		}
	}

	public String getId() {
		return this.groupByAttribute + Constants.spacer + Constants.spacer + this.aggregateAttribute;
	}
	
	public View constituentView(AggregateFunctions f) {
		if (f == AggregateFunctions.ALL) return this;
		AggregateGroupByView view = new AggregateGroupByView(this.groupByAttribute, this.aggregateAttribute);
		for (String key : this.aggregateValues.keySet()) {
			AggregateValuesWrapper wrapper = aggregateValues.get(key);
			AggregateValuesWrapper newWrapper = new AggregateValuesWrapper();
			view.func = f;
			switch (f) {
			case COUNT:
				for (int i = 0; i < 2; i++) {
					newWrapper.datasetValues[i].generic = wrapper.datasetValues[i].count;
					newWrapper.datasetValues[i].genericNormalized = wrapper.datasetValues[i].countNormalized;
				}
				break;
			case SUM:
				for (int i = 0; i < 2; i++) {
					newWrapper.datasetValues[i].generic = wrapper.datasetValues[i].sum;
					newWrapper.datasetValues[i].genericNormalized = wrapper.datasetValues[i].sumNormalized;
				}
				break;
			case AVG:
				for (int i = 0; i < 2; i++) {
					newWrapper.datasetValues[i].generic = wrapper.datasetValues[i].average;
					newWrapper.datasetValues[i].genericNormalized = wrapper.datasetValues[i].averageNormalized;
				}
				break;
			}
			view.aggregateValues.put(key, newWrapper);
		}
		return view;
	}

	@Override
	public List<View> constituentViews() {
		populateAvg();
		normalize();
		List<View> ret = Lists.newArrayList();
		ret.add(constituentView(AggregateFunctions.COUNT));
		ret.add(constituentView(AggregateFunctions.SUM));
		ret.add(constituentView(AggregateFunctions.AVG));
		return ret;
	}
	
	public AggregateFunctions getFunction() {
		return func;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public void setFunction(String function) {
		if (function.equals("AVG")){
			func = AggregateFunctions.AVG;
		} else if (function.equals("COUNT")){
			func = AggregateFunctions.COUNT;
		} else if (function.equals("SUM")){
			func = AggregateFunctions.SUM;
		}
	}
}
