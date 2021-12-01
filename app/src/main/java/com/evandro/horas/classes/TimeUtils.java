package com.evandro.horas.classes;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.util.Locale;

public class TimeUtils {

    public static int balanceMin(String en, String ien, String iex, String ex) {
        int tot;
        if(!en.equals("") && ien.equals("") && iex.equals("") && ex.equals("")) {
            tot = 480 - toMin(en);
        } else if(!en.equals("") && !ien.equals("") && iex.equals("") && ex.equals("")) {
            tot = 480 - toMin(en);
        } else if(!en.equals("") && !ien.equals("") && !iex.equals("") && ex.equals("")) {
            tot = 540 - TimeUtils.toMin(en) - (TimeUtils.toMin(iex) - TimeUtils.toMin(ien));
        } else {
            tot = ((toMin(ex) - toMin(en)) - (toMin(iex) - toMin(ien))) - 528;
        }
        if(Math.max(-5, tot) == Math.min(tot, 5)) {
            tot = 0;
        }
        return tot;
    }

    public static int totalMin(String en, String ien, String iex, String ex) {
        int e1 = 0, ie1 = 0, ie2 = 0, e2 = 0;
        if(!en.equals("")) { e1 = toMin(en); }
        if(!ien.equals("")) { ie1 = toMin(ien); }
        if(!iex.equals("")) { ie2 = toMin(iex); }
        if(!ex.equals("")) { e2 = toMin(ex); }
        int tot = (ie1 - e1) + (e2 - ie2);
        return tot;
    }

    public static int toMin(String hora) {
        String [] ha = hora.split(":");
        int h = Integer.parseInt(ha[0]) * 60;
        return h + Integer.parseInt(ha[1]);
    }

    public static String toHour(int min) {
        int hour = min / 60;
        min = min - (hour * 60);
        if(hour < 0) Integer.toString(hour).substring(1);
        if(min < 0) Integer.toString(min).substring(1);
        DecimalFormat f = new DecimalFormat("00");
        String h = f.format(hour);
        String m = f.format(min);
        String time;
        if(m.length() == 3) {
            m = m.substring(1);
            if(h.length() == 3) h = h.substring(1);
            time = "-"+h+":"+m;
        } else {
            time = h+":"+m;
        }
        return time;
    }

    public static String getDate() {
        DateTime today = new DateTime();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        return today.toString(dtf);
    }

    public static String moreDay(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);
        DateTime dateMoreDays = date.plusDays(1);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        return dateMoreDays.toString(dtf);
    }

    public static String lessDay(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);
        DateTime dateMoreDays = date.minusDays(1);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        return dateMoreDays.toString(dtf);
    }

    public static String moreDayWeek(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);
        DateTime newDate = date.plusDays(1);

        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("d/E");
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("MMMM/Y");
        DateTimeFormatter dtf3 = DateTimeFormat.forPattern("dd/MM/yyyy");

        return newDate.toString(dtf1)+"-"+newDate.toString(dtf2)+"-"+newDate.toString(dtf3);
    }

    public static String lessDayWeek(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);
        DateTime newDate = date.minusDays(1);

        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("d/E");
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("MMMM/Y");
        DateTimeFormatter dtf3 = DateTimeFormat.forPattern("dd/MM/yyyy");

        return newDate.toString(dtf1)+"-"+newDate.toString(dtf2)+"-"+newDate.toString(dtf3);
    }

    public static String moreMonth(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);
        DateTime dateMoreMonth = date.plusMonths(1);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM/Y");
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("dd/MM/yyyy");

        return dateMoreMonth.toString(dtf)+"-"+dateMoreMonth.toString(dtf2);
    }

    public static String lessMonth(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);
        DateTime dateMinusMonth = date.minusMonths(1);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM/Y");
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("dd/MM/yyyy");

        return dateMinusMonth.toString(dtf)+"-"+dateMinusMonth.toString(dtf2);
    }

    public static String getDayWeekString(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);

        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("d/E");

        return date.toString(dtf1);
    }

    public static String getMonthYearString(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM/Y");

        return date.toString(dtf);
    }

    public static String getMonthYearDate(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);

        if(date.getDayOfMonth() >= 16 ) {
            date = date.plusMonths(1);
        }

        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM-yy");

        return date.toString(dtf);
    }

    public static int compareTo(String d1, String d2) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime thisDate = format.parseDateTime(d1);
        DateTime otherDate = format.parseDateTime(d2);
        return thisDate.compareTo(otherDate);
    }

}
