package au.edu.cdu.dynamicproblems.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.util.LogUtil;

public class SQLiteDBOperation implements DBOperation {
	private static Logger log = LogUtil.getLogger(SQLiteDBOperation.class);
	
	Connection c;

	public Connection getC() {
		return c;
	}

	public void openConnection() {
		if (c != null) {
			return;
		}
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:konet.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	public void closeConnection() throws SQLException {
		if (c != null) {
			c.close();
		}
	}

	public void insert(UpdateDataInfo info) throws SQLException {
		if (c == null) {
			openConnection();
		}
		Statement stmt = null;
		stmt = c.createStatement();
		StringBuffer sqlSB = new StringBuffer();
		StringBuffer columnNameSB = new StringBuffer();
		StringBuffer columnValueSB = new StringBuffer();
		sqlSB.append("insert into ").append(info.getTableName()).append("(");

		Map<String, String> infoMap = info.getColumns();
		Set<String> columnNames = infoMap.keySet();

		for (String columnName : columnNames) {
			columnNameSB.append(columnName).append(",");
			String columnValue = infoMap.get(columnName);
			columnValueSB.append(columnValue).append(",");

		}

		sqlSB.append(columnNameSB.substring(0, columnNameSB.length() - 2))
				.append(") values(")
				.append(columnValueSB.substring(0, columnValueSB.length() - 2))
				.append(");");

	    log.debug(sqlSB);
		
		stmt.executeUpdate(sqlSB.toString());

		stmt.close();
		c.commit();
	}

}

class UpdateDataInfo {
	String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, String> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, String> columns) {
		this.columns = columns;
	}

	Map<String, String> columns;
}
