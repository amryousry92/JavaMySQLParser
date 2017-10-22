package com.ef.parser;

import com.ef.database.JDBCObject;
import com.ef.database.LogEntry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author user
 */
public class Parser {

    static JDBCObject jdbcObject = new JDBCObject();
//    private static

    private static void readFile(String fileName) throws Exception {
        List<LogEntry> logEntries = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            //1. filter line 3
            //2. convert all content to upper case
            //3. convert it into a List
            stream.forEach(line -> {
                LogEntry logEntry = new LogEntry();
                try {
                    logEntry.setDateString(line.split("|")[0]);
                    logEntry.setIp(line.split("|")[1]);
                    logEntry.setRequest(line.split("|")[2]);
                    logEntry.setStatus(line.split("|")[3]);
                    logEntry.setAgent(line.split("|")[4]);
                    logEntries.add(logEntry);
                } catch (ParseException e) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, e);
                }
            });
            jdbcObject.addDataToDB(logEntries);
        } catch (IOException e) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static void initializeConnection() throws SQLException {
        jdbcObject.makeJDBCConnection();
    }

    private static String constructWhereStatement(String startDate, String duration, String threshold) throws ParseException {
        long epochDate = getDateEpoch(startDate);
        long endDate = (duration == "hourly") ? epochDate + 3600000l : epochDate + 86400000l;
        String whereStatement = "date>=" + epochDate + " date<=" + endDate + " AND COUNT(*)>=" + threshold;
        return whereStatement;
    }

    private static long getDateEpoch(String dateString) throws ParseException {
//        yyyy-MM-dd HH:mm:ss.SSS
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = sdf.parse(dateString);
        return date.getTime();
    }

    private static void printIps(List<LogEntry> logsEntries) {
        for (LogEntry logEntry : logsEntries) {
            System.out.println(logEntry.getIp());
        }
    }

    public static void main(String[] args) {
        try {
            initializeConnection();
            String startDate = "2017-01-01.13:00:00";
            String duration = "hourly";
            String threshold = "200";
            String fileName = "F:\\Java_MySQL_Test\\access.log";
            readFile(fileName);//reads log file and loads it into table logs
            String whereStatement = constructWhereStatement(startDate, duration, threshold);
            List<LogEntry> logsEntries = jdbcObject.getDataFromDB(whereStatement);
            printIps(logsEntries);
        } catch (Exception ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
