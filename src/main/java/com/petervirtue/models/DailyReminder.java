package com.petervirtue.models;

/*

Daily Reminder Model:
- id (int, PK)
- user_id (int, FK)
- days_left (int)
- message (varchar(128))
- time (time)
- created_at (timestamp)
- updated_at (timestamp)

*/

import java.sql.Time;
import java.util.Date;

import com.petervirtue.utils.DateTime;

public class DailyReminder extends ScheduleRule {

    public DailyReminder(User user, String message, Time alertTime, int daysLeft) {
        super(user, message, alertTime, daysLeft);
    }

    public boolean shouldAlertOnDay(Date current) {
        // Logic on this model:
        // - -1 days left is a daily reminder with no time table
        // - 0 - n days left is a daily reminder with a certain number of days left to send the reminder
        if (daysLeft > 0 || daysLeft == -1) {
            return true;
        }

        return false;
    }

    public String getLocalizedReminder() {
        String line = "- Daily reminder of \"" + message + "\" at " + DateTime.trimDate(alertTime);

        if ( daysLeft != -1) {
            line += " for " + daysLeft + " more days";
        }
        
        return line;
    }
}
