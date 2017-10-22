package com.database;

import com.configuration.Configurations;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import repository.LogEntryRepository;
import repository.LogErrorEntryRepository;

/**
 * @author .com Simple Hello World MySQL Tutorial on how to make JDBC
 * connection, Add and Retrieve Data by App Shah
 *
 */
public class JDBCObject {

//    static Connection conn;
//    static PreparedStatement prepareStat;
//    private final Configurations props;
    @Autowired
    LogEntryRepository logEntryRepository;

    @Autowired
    LogErrorEntryRepository logErrorEntryRepository;

    public JDBCObject() {
//        this.props = Configurations.getInstance("config.properties");
//        this.prepareStat = null;
//        this.conn = null;
    }

//    public void makeJDBCConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//        String databaseUrl = props.getProperty("mysql.server.url");
//        String databasePort = props.getProperty("mysql.server.port");
//        String database = props.getProperty("mysql.database");
//        String username = props.getProperty("mysql.username");
//        String password = props.getProperty("mysql.password");
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        conn = DriverManager.getConnection(databaseUrl + ":" + databasePort + "/" + database, username, password);
//    }
    public void addDataToDB(List<LogEntry> entries) throws SQLException {

//        String insertQueryStatement = "INSERT INTO logs VALUES ";
//        Statement statement = conn.createStatement();
//        String query = "";
//        for (LogEntry entry : entries) {
//            query = insertQueryStatement + "(";
//            query += entry.getArrival_date() + ",";
//            query += "'" + entry.getIp() + "',";
//            query += "'" + entry.getRequest() + "',";
//            query += "'" + entry.getStatus() + "',";
//            query += "'" + entry.getAgent() + "')";
//            statement.addBatch(query);
//        }
//        statement.executeBatch();
//        entries.forEach(entry -> {
//            logEntryRepository.save(entry);
//            logEntryRepository.flush();
//        });
        logEntryRepository.save(entries);
    }

    public List<String> getDataFromDB(long startDate, long endDate, int threshold) throws SQLException {

//        String insertQueryStatement = "INSERT INTO error_logs VALUES (";
//        Statement statement = conn.createStatement();
//        String query = "";
//        String getQueryStatement = "SELECT * FROM logs";
//        getQueryStatement += remainingQueryStatement;
//
//        prepareStat = conn.prepareStatement(getQueryStatement);
//        ResultSet rs = prepareStat.executeQuery();
//        while (rs.next()) {
//            LogEntry dbEntry = new LogEntry();
//            dbEntry.setArrival_date(rs.getLong("date"));
//            dbEntry.setIp(rs.getString("ip"));
//            dbEntry.setRequest(rs.getString("request"));
//            dbEntry.setStatus(rs.getString("status"));
//            dbEntry.setAgent(rs.getString("agent"));
//            outputList.add(dbEntry);
//            query = insertQueryStatement;
//            query += "'" + dbEntry.getIp() + "', 'Unreachable')";
//            statement.addBatch(query);
//        }
//        statement.executeBatch();
        List<String> outputList = logEntryRepository.findIpsByDate(startDate, endDate, threshold);

        return outputList;
    }

    public void saveToErrorLog(List<String> ipList) {
        List<LogErrorEntry> logErrorEntries = new ArrayList<>();
        ipList.forEach(ip -> {
            LogErrorEntry logErrorEntry = new LogErrorEntry();
            logErrorEntry.setIp(ip);
            logErrorEntry.setComment("Connection failed");
            logErrorEntries.add(logErrorEntry);
//            logErrorEntryRepository.save(logErrorEntry);
//            logErrorEntryRepository.flush();
        });
        logErrorEntryRepository.save(logErrorEntries);
    }
}
