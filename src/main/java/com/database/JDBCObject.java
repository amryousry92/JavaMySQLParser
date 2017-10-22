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

public class JDBCObject {

    @Autowired
    LogEntryRepository logEntryRepository;

    @Autowired
    LogErrorEntryRepository logErrorEntryRepository;

    public JDBCObject() {
    }
    public void addDataToDB(List<LogEntry> entries) throws SQLException {

        logEntryRepository.save(entries);
    }

    public List<String> getDataFromDB(long startDate, long endDate, int threshold) throws SQLException {
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
        });
        logErrorEntryRepository.save(logErrorEntries);
    }
}
