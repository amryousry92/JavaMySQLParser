package com.database;

import com.configuration.Configurations;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCObject {

    static Connection conn;
    static PreparedStatement prepareStat;
    private final Configurations props;

    public JDBCObject() {
        this.props = Configurations.getInstance("config.properties");
        this.prepareStat = null;
        this.conn = null;
    }
// initializes connection with database

    public void makeJDBCConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String databaseUrl = props.getProperty("mysql.server.url");
        String databasePort = props.getProperty("mysql.server.port");
        String database = props.getProperty("mysql.database");
        String username = props.getProperty("mysql.username");
        String password = props.getProperty("mysql.password");
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conn = DriverManager.getConnection(databaseUrl + ":" + databasePort + "/" + database, username, password);
    }
// adding data to logs table

    public void addDataToDB(List<LogEntry> entries) throws SQLException {

        String insertQueryStatement = "INSERT INTO logs VALUES ";
        Statement statement = conn.createStatement();
        String query = "";
        for (LogEntry entry : entries) {
            query = insertQueryStatement + "(";
            query += entry.getArrival_date() + ",";
            query += "'" + entry.getIp() + "',";
            query += "'" + entry.getRequest() + "',";
            query += "'" + entry.getStatus() + "',";
            query += "'" + entry.getAgent() + "')";
            statement.addBatch(query);
        }
        statement.executeBatch();
    }
// getting ips from logs table

    public List<LogEntry> getDataFromDB(String remainingQueryStatement) throws SQLException {

        List<LogEntry> outputList = new ArrayList<>();
        String insertQueryStatement = "INSERT INTO error_logs VALUES (";
        Statement statement = conn.createStatement();
        String query = "";
        String getQueryStatement = "SELECT * FROM logs";
        getQueryStatement += remainingQueryStatement;

        prepareStat = conn.prepareStatement(getQueryStatement);
        ResultSet rs = prepareStat.executeQuery();
        while (rs.next()) {
            LogEntry dbEntry = new LogEntry();
            dbEntry.setArrival_date(rs.getLong("arrival_date"));
            dbEntry.setIp(rs.getString("ip"));
            dbEntry.setRequest(rs.getString("request"));
            dbEntry.setStatus(rs.getString("status"));
            dbEntry.setAgent(rs.getString("agent"));
            outputList.add(dbEntry);
            query = insertQueryStatement;
            query += "'" + dbEntry.getIp() + "', 'IP Unreachable')";
            statement.addBatch(query);
        }
        statement.executeBatch();
        return outputList;
    }

}
