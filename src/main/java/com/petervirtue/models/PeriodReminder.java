package com.petervirtue.models;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import com.petervirtue.utils.DateTime;

public class PeriodReminder extends ScheduleRule {

    private int period;
    private boolean monthly;

    public PeriodReminder(User user, String message, Time alertTime, int daysLeft, int period, boolean monthly) {
        this.user = user;
        this.message = message;
        this.alertTime = alertTime;
        this.daysLeft = daysLeft;
        this.period = period;
        this.monthly = monthly;
    }

    public boolean shouldAlertOnDay(Date current) {
        // Logic on this model:
        // - If not monthly, daysLeft being 0 indicates that we alert on that day
        // - If monthly, we get the day of the month and compare it to the period to check if we alert or not
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);

        if ((daysLeft == 0 && !monthly) || (monthly && period == calendar.get(Calendar.DAY_OF_MONTH))) {
            return true;
        }

        return false;
    }

    public int getPeriod() {
        return period;
    }

    public boolean getMonthly() {
        return monthly;
    }

    public String getLocalizedReminder() {
        String line = "";

        if (monthly) {
            line = "- Montly reminder of \"" + message + "\" at " + DateTime.trimDate(alertTime) + " on day " + period + " of every month";
        } else if (period == 6) {
            line = "- Weekly reminder of \"" + message + "\" at " + DateTime.trimDate(alertTime) + " on " + DateTime.getWeekdayFromDaysLeft(daysLeft) + " of every week";
        } else {
            line = "- Reminder of \"" + message + "\" at " + DateTime.trimDate(alertTime) + " every " + period + " days";
        }

        return line;
    }
    
}
