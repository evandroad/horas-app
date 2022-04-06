package com.evandro.horas.classes;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Register implements Comparable<Register> {

    private String date;
    private String entry;
    private String intervalEntry;
    private String intervalExit;
    private String exit;

    public Register(String date, String entry, String intervalEntry, String intervalExit, String exit) {
        this.date = date;
        this.entry = entry;
        this.intervalEntry = intervalEntry;
        this.intervalExit = intervalExit;
        this.exit = exit;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getEntry() { return entry; }
    public void setEntry(String entry) { this.entry = entry; }

    public String getIntervalEntry() { return intervalEntry; }
    public void setIntervalEntry(String intervalEntry) { this.intervalEntry = intervalEntry; }

    public String getIntervalExit() { return intervalExit; }
    public void setIntervalExit(String intervalExit) { this.intervalExit = intervalExit; }

    public String getExit() { return exit; }
    public void setExit(String exit) { this.exit = exit; }

    @Override
    public String toString() {
        return "    {" +
                "\"date\":\"" + date +
                "\",\"entry\":\"" + entry +
                "\",\"intervalEntry\":\"" + intervalEntry +
                "\",\"intervalExit\":\"" + intervalExit +
                "\",\"exit\":\"" + exit +
                "\"}";
    }

    @Override
    public int compareTo(Register other) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime thisDate = format.parseDateTime(this.date);
        DateTime otherDate = format.parseDateTime(other.date);
        return thisDate.compareTo(otherDate);
    }

}