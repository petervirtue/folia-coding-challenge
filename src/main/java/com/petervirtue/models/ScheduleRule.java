package com.petervirtue.models;

import java.sql.Time;
import java.util.Date;

public abstract class ScheduleRule {

    // Everything that both rules use
    protected User user;
    protected String message;
    protected Time alertTime;
    protected int daysLeft;

    public ScheduleRule(User user, String message, Time alertTime, int daysLeft) {
        this.user = user;
        this.message = message;
        this.alertTime = alertTime;
        this.daysLeft = daysLeft;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Time getAlertTime() {
        return alertTime;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    // Abstract methods defined on the individual level
    public abstract boolean shouldAlertOnDay(Date current);

    public abstract String getLocalizedReminder();

}
