package common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;
import db_wrappers.DBConnection;

/**
 * Obtains metadata about attributes. Currently only determines if attribute
 * is measure or dimension. 
 * 
 * TODO: determine number of distinct values, type of attribute etc.
 * @author manasi
 *
 */
public class InputTablesMetadata {
	private List<Attribute> dimensionAttributes;
	private List<Attribute> measureAttributes;
	private List<String> tables;
	private List<String> dimensions = new ArrayList<String>();
	private List<String> measures =  new ArrayList<String>();
	private int numRows;
	
	public int getNumRows() {
		return numRows;
	}
	
	public List<Attribute> getDimensionAttributes() {
		return dimensionAttributes;
	}
	
	public List<Attribute> getMeasureAttributes() {
		return measureAttributes;
	}

	public InputTablesMetadata(List<String> tables, 
			DBConnection connection, List<String> dimensions, List<String> measures) {
		dimensionAttributes = Lists.newArrayList();
		measureAttributes = Lists.newArrayList();
		this.tables = tables;
		this.dimensions = dimensions;
		this.measures = measures;
		
		getAttributeMetadataByName(connection);
		//getAttributeMetadataByCount(connection);
		this.numRows = queryForNumRows(connection);
	}
	
	

	private int queryForNumRows(DBConnection connection) {
		ResultSet rs = connection.getNumRows(tables.get(0));
		try {
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

	private void getAttributeMetadataByName(DBConnection connection) {
		for (String table : tables) {
			ResultSet rs = connection.getTableColumns(table);
			try {
				while (rs.next()) {
					String attribute = rs.getString("COLUMN_NAME");
					if (this.measures.contains(attribute)){
						this.measureAttributes.add(new Attribute(attribute));
					}
					if (this.dimensions.contains(attribute)){
						this.dimensionAttributes.add(new Attribute(attribute));
					}
				}
			} catch (SQLException e) {
				continue;
			}
		}
		
	}
}
