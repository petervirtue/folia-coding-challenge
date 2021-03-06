package com.petervirtue.models;

/*

Period Reminder Model:
- id (int, PK)
- user_id (int, FK)
- days_left (int)
- message (varchar(128))
- time (time)
- period (int)
- monthly (boolean, tinyint)
- created_at (timestamp)
- updated_at (timestamp)

*/

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import com.petervirtue.utils.DateTime;

public class PeriodReminder extends Reminder {

    private int period;
    private boolean monthly;

    public PeriodReminder(User user, String message, Time alertTime, int daysLeft, int period, boolean monthly) {
        super(user, message, alertTime, daysLeft);
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
