package com.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Amr
 */
public class LogEntry {

    private long arrival_date;

    private String ip;
    private String request;
    private String status;
    private String agent;

    public LogEntry(){
    }
    
    public long getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(long arrival_date) {
        this.arrival_date = arrival_date;
    }    

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public void setDateString(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date formattedDate = sdf.parse(dateString);
        this.arrival_date = formattedDate.getTime();
    }

    public String getDateString() {

        Date epochDate = new Date(this.arrival_date);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        format.setTimeZone(TimeZone.getTimeZone("Cest/UTC"));
        String formatted = format.format(epochDate);
        return formatted;
    }
}
