import java.sql.*;
import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel{

	private Statement statement;
	private ResultSet resultset;
	private ResultSetMetaData metaData;
	private int numberOfRows; 
	
	public ResultSetTableModel(String query, Connection connection) 
		throws SQLException, ClassNotFoundException{
			try {
				
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
				setQuery(query);
			
				//setUpdate(query);
				
			}catch(Exception e) {
				System.out.println("1");
			}
	}
	
	public Class getColumnClass(int column) {
		try {
			String className = metaData.getColumnClassName(column+1);
			
			return Class.forName(className);
		}catch(Exception e) {
			System.out.println("7");
		}
		
		return Object.class;
	}
	
	public int getColumnCount(){
		try {
			return metaData.getColumnCount();
		} catch(Exception e){
			System.out.println("2");
		}
		
		return 0;
	}
	
	public String getColumnName(int column) {
		try {
			return metaData.getColumnName(column+1);
		}catch(Exception e){
			System.out.println(e);
		}
		
		return "";
	}
	
	public int getRowCount() {
		return numberOfRows;
	}
	
	public Object getValueAt(int row, int col) {
		try {
			resultset.next();
			resultset.absolute(row+1);
			return resultset.getObject(col+1);
		}catch(Exception e) {
			System.out.println("4");
		}
		
		return "";
	}
	
	public void setQuery(String query) {
		try {
			resultset = statement.executeQuery(query);
			metaData = resultset.getMetaData();
			resultset.last();
			numberOfRows = resultset.getRow();
			fireTableStructureChanged();
		} catch (SQLException e) {
			System.out.println("5");
		}
	}
	
	public void setUpdate(String query) {
		int res;
		try {
			res = statement.executeUpdate(query);
			metaData = resultset.getMetaData();
			resultset.last();
			numberOfRows = resultset.getRow();
			fireTableStructureChanged();
		} catch (SQLException e) {
			System.out.println("6");
		}
	}
	
	public void disconnecFromDatabase() {
		try {
			statement.close();
			
		}catch(Exception e) {
			System.out.println(":");
		}
		finally {
		}
	}
}













