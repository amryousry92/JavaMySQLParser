/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ef.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Amr
 */
public class DBEntry {

    private long date;
    private String ip;
    private String request;
    private String status;
    private String agent;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = sdf.parse(dateString);
        this.date = date.getTime();
    }

    public String getDateString() {

        Date date = new Date(this.date);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Cest/UTC"));
        String formatted = format.format(date);
        return formatted;
    }
}
