package com.evandro.horas.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class TimeUtils {

    public static final int EQUAL = 0;
    public static final int MAJOR = 1;
    public static final int MINOR = -1;

    public static int balanceMin(String en, String ien, String iex, String ex) {
        int tot;
        if(!en.equals("") && ien.equals("") && iex.equals("") && ex.equals("")) {
            tot = 480 - toMin(en);
        } else if(!en.equals("") && !ien.equals("") && iex.equals("") && ex.equals("")) {
            tot = (toMin(ien) - toMin(en)) - 528;
        } else if(!en.equals("") && ien.equals("") && iex.equals("") && !ex.equals("")) {
            tot = (toMin(ex) - toMin(en)) - 528;
        } else if(!en.equals("") && !ien.equals("") && !iex.equals("") && ex.equals("")) {
            tot = 540 - toMin(en) - (toMin(iex) - toMin(ien));
        } else if(!en.equals("") && !ien.equals("") && !iex.equals("") && !ex.equals("")) {
            tot = ((toMin(ex) - toMin(en)) - (toMin(iex) - toMin(ien))) - 528;
        } else {
            tot = 0;
        }
        if(Math.max(-5, tot) == Math.min(tot, 5)) {
            tot = 0;
        }
        return tot;
    }

    public static int totalMin(String en, String ien, String iex, String ex) {
        Date date = new Date();
        int tot = 0;

        if(!en.equals("") && ien.equals("") && iex.equals("") && ex.equals("")) {
            tot = toMin(date.getHours()+":"+date.getMinutes()) - toMin(en);
        }else if(!en.equals("") && !ien.equals("") && iex.equals("") && ex.equals("")) {
            tot = toMin(ien) - toMin(en);
        }else if(!en.equals("") && ien.equals("") && iex.equals("") && !ex.equals("")) {
            tot = toMin(ex) - toMin(en);
        } else if(!en.equals("") && !ien.equals("") && !iex.equals("") && ex.equals("")) {
            tot = (toMin(ien) - toMin(en)) + toMin(date.getHours()+":"+date.getMinutes()) - toMin(iex);
        } else if(!en.equals("") && !ien.equals("") && !iex.equals("") && !ex.equals("")) {
            tot = (toMin(ex) - toMin(en)) - (toMin(iex) - toMin(ien));
        }

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

    public static int getDayWeek(String today) {
        GregorianCalendar gc = new GregorianCalendar();
        try {
            gc.setTime(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"))
                    .parse(today)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gc.get(Calendar.DAY_OF_WEEK);
    }

    public static String moreDay(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);
        DateTime dateMoreDays = date.plusDays(1);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        return dateMoreDays.toString(dtf);
    }

    public static String moreTwoDay(String today) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = format.parseDateTime(today);
        DateTime dateMoreDays = date.plusDays(2);

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
