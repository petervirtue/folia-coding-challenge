package com.petervirtue.utils;

import java.util.Calendar;
import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTime {

    // Weekday int to String
    public static String getLocalizedWeekday(int day) {
        String weekday = "";

        switch(day) {
            case 1:
                weekday = "Sunday";
                break;
            case 2:
                weekday = "Monday";
                break;
            case 3:
                weekday = "Tuesday";
                break;
            case 4:
                weekday = "Wednesday";
                break;
            case 5:
                weekday = "Thursday";
                break;
            case 6:
                weekday = "Friday";
                break;
            case 7:
                weekday = "Saturday";
                break;
        }

        return weekday;
    }

    // Get weekday for reminder based off of days left & current weekday
    public static String getWeekdayFromDaysLeft(int daysLeft) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        
        if (weekday + daysLeft > 7) {
            weekday = weekday + daysLeft - 7;
        } else {
            weekday = weekday + daysLeft;
        }

        return getLocalizedWeekday(weekday);
    }

    // Get days left given target weekday
    public static int getDaysLeft(int target) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int daysLeft = (target - weekday < 0) ? (7 - (weekday - target)) : (daysLeft = target - weekday);
        return daysLeft;
    }

    // Get the time for the reminder
    public static String trimDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    // Get time from string
    public static Time getTimeFromString(String stringTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Time time = null;
        try {
            time = new Time(simpleDateFormat.parse(stringTime).getTime());
        } catch (ParseException e) {
            return null;
        }
        
        return time;
    }

    // Get date from string
    public static Date getDateFromString(String stringDate)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            return null;
        }

        return date;
    }
    
}
