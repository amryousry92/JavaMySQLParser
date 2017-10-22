package com.ef.database;

import com.ef.parser.Parser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author .com Simple Hello World MySQL Tutorial on how to make JDBC
 * connection, Add and Retrieve Data by App Shah
 *
 */
public class JDBCObject {

    static Connection conn = null;
    static PreparedStatement prepareStat = null;

    public JDBCObject() {

    }

    public void makeJDBCConnection() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_mysql_parser", "root", "root");
    }

    public void addDataToDB(List<LogEntry> entries) throws SQLException {

        String insertQueryStatement = "INSERT INTO logs VALUES (?,?,?,?,?)";
        Statement statement = conn.createStatement();
        for (LogEntry entry : entries) {
            prepareStat = conn.prepareStatement(insertQueryStatement);
            prepareStat.setLong(1, entry.getDate());
            prepareStat.setString(2, entry.getIp());
            prepareStat.setString(3, entry.getRequest());
            prepareStat.setString(4, entry.getStatus());
            prepareStat.setString(5, entry.getAgent());
            statement.addBatch(prepareStat.toString());
        }
        // execute insert SQL statement
        statement.executeBatch();
    }

    public List<LogEntry> getDataFromDB(String where) throws SQLException {

        List<LogEntry> outputList = new ArrayList<>();
        String insertQueryStatement = "INSERT INTO error_logs VALUES (?,?)";
        Statement statement = conn.createStatement();

        String getQueryStatement = "SELECT * FROM logs";
        if (!"".equals(where)) {
            getQueryStatement += " WHERE " + where;
            getQueryStatement += " GROUP BY ip";
        }
        prepareStat = conn.prepareStatement(getQueryStatement);
        ResultSet rs = prepareStat.executeQuery();
        while (rs.next()) {
            LogEntry dbEntry = new LogEntry();
            dbEntry.setDate(rs.getLong("date"));
            dbEntry.setIp(rs.getString("ip"));
            dbEntry.setRequest(rs.getString("request"));
            dbEntry.setStatus(rs.getString("status"));
            dbEntry.setAgent(rs.getString("agent"));
            outputList.add(dbEntry);
            prepareStat = conn.prepareStatement(insertQueryStatement);
            prepareStat.setString(1, dbEntry.getIp());
            prepareStat.setString(2, "Unreachable");
            statement.addBatch(prepareStat.toString());
        }
        statement.executeBatch();

        return outputList;
    }

}
