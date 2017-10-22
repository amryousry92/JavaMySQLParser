/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "logs")
public class LogEntry implements Serializable {

    private static final long serialVersionUID = 1125097495560335087L;
    private long arrival_date;

    private String ip;
    private String request;
    private String status;
    private String agent;
    @Id
    private Long id;

    public LogEntry() {
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
//        yyyy-MM-dd HH:mm:ss.SSS
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = sdf.parse(dateString);
        this.arrival_date = date.getTime();
    }

    public String getDateString() {

        Date date = new Date(this.arrival_date);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        format.setTimeZone(TimeZone.getTimeZone("Cest/UTC"));
        String formatted = format.format(date);
        return formatted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
