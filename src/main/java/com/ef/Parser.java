package com.ef;

import com.configuration.Configurations;
import com.database.JDBCObject;
import com.database.LogEntry;
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
    private final static Configurations configurations = Configurations.getInstance("config.properties");

//    private static
    private static void readFile(String fileName) throws Exception {
        List<LogEntry> logEntries = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> {
                LogEntry logEntry = new LogEntry();
                try {
                    line = line.replaceAll("\"", "");
                    logEntry.setDateString(line.split("\\|")[0]);
                    logEntry.setIp(line.split("\\|")[1]);
                    logEntry.setRequest(line.split("\\|")[2]);
                    logEntry.setStatus(line.split("\\|")[3]);
                    logEntry.setAgent(line.split("\\|")[4]);
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

    private static void initializeConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        jdbcObject.makeJDBCConnection();
    }

    private static String constructQueryMissingStatements(String startDate, String duration, String threshold) throws ParseException {
        long epochDate = getDateEpoch(startDate);
        long endDate = ("hourly".equals(duration)) ? epochDate + 3600000l : epochDate + 86400000l;
        String statement = " WHERE date>=" + epochDate + " AND date<=" + endDate;
        statement += " GROUP BY ip";
        statement += " HAVING COUNT(*)>=" + Integer.parseInt(threshold);
        return statement;
    }

    private static long getDateEpoch(String dateString) throws ParseException {
//        yyyy-MM-dd HH:mm:ss.SSS
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
        Date date = sdf.parse(dateString);
        return date.getTime();
    }

    private static void printIps(List<LogEntry> logsEntries) {
        logsEntries.forEach((logEntry) -> {
            System.out.println(logEntry.getIp());
        });
    }

    public static String getPropValue(String key) {
        String args = System.getProperty("sun.java.command");
        return args.split(key + "=")[1].split(" ")[0];
    }

    public static void main(String[] args) {
        try {
            initializeConnection();
//            String startDate = "2017-01-01.13:00:00";
//            String duration = "hourly";
//            String threshold = "200";
//            String fileName = configurations.getProperty("log.file.path");
            String startDate = getPropValue("startDate");
            String duration = getPropValue("duration");
            String threshold = getPropValue("threshold");
            String fileName = getPropValue("accesslog");
            readFile(fileName);//reads log file and loads it into table logs
            String whereStatement = constructQueryMissingStatements(startDate, duration, threshold);
            List<LogEntry> logsEntries = jdbcObject.getDataFromDB(whereStatement);
            printIps(logsEntries);
        } catch (Exception ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
