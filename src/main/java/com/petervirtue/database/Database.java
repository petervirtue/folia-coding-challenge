package com.petervirtue.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.petervirtue.models.DailyReminder;
import com.petervirtue.models.PeriodReminder;
import com.petervirtue.models.ScheduleRule;
import com.petervirtue.models.User;

public class Database {

    // Database connection, users and rules
    private Connection connection = null;
    private ArrayList<User> users = null;
    private ArrayList<ScheduleRule> rules = null;

    // Initialize
    public Database(String url, String user, String password) {
        // Takes provided credentials and attempts to create a connection
        try {
            connection = DriverManager.getConnection(url, user, password);

            // Populate Users and Rules from DB
            getUsers();
            getRules();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all users for the DB instance
    private void getUsers() {
        ArrayList<User> newUsers = new ArrayList<User>();
        String getUsers = "SELECT id, name FROM users";

        // Try to get the users
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getUsers);

            while (resultSet.next()) {
                User user = new User(resultSet.getInt("id"), resultSet.getString("name"));
                newUsers.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        users = newUsers;
    }

    // Get all rules for the DB instance
    private void getRules() {
        ArrayList<ScheduleRule> newRules = new ArrayList<ScheduleRule>();

        // Try to get the rules
        try {
            Statement statement = connection.createStatement();

            // Daily Reminders
            String getDaily = "SELECT * FROM daily_reminders";
            ResultSet resultSet = statement.executeQuery(getDaily);

            while (resultSet.next()) {
                User user = getUserByID(resultSet.getInt("user_id"));

                if (user != null) {
                    DailyReminder reminder = new DailyReminder(
                        user,
                        resultSet.getString("message"),
                        resultSet.getTime("time"),
                        resultSet.getInt("days_left")
                    );
                    newRules.add(reminder);
                }
            }

            // Period Reminders
            String getPeriod = "SELECT * FROM period_reminders";
            resultSet = statement.executeQuery(getPeriod);

            while (resultSet.next()) {
                User user = getUserByID(resultSet.getInt("user_id"));

                if (user != null) {
                    PeriodReminder reminder = new PeriodReminder(
                        user,
                        resultSet.getString("message"),
                        resultSet.getTime("time"),
                        resultSet.getInt("days_left"),
                        resultSet.getInt("period"),
                        resultSet.getBoolean("monthly")
                    );
                    newRules.add(reminder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        rules = newRules;
    }

    // Database Component Get All for Users
    public ArrayList<User> getAllUsers() {
        return users;
    }

    // Database Component Get All for Schedule Rules
    public ArrayList<ScheduleRule> getAllScheduleRules() {
        return rules;
    }

    // Database Component Save for Users
    public int save(User user) {
        // If no users are edited, 0 is returned for the ID. Otherwise, the affected ID is returned.
        String saveUser = "INSERT INTO users(name) "
            + "VALUES(?)";
        int id = 0;

        try {
            // Prepared stamement so that we can see if rows are changed or not.
            // Realistically with this program there won't be editing of users,
            // but in practice this is how I have done it before
            PreparedStatement statement = connection.prepareStatement(saveUser, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            int rowsChanged = statement.executeUpdate();

            if (rowsChanged > 0) {
                try {
                    ResultSet resultSet = statement.getGeneratedKeys();

                    if (resultSet.next()) {
                        id = resultSet.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Refresh users
        getUsers();

        return id;
    }

    // Database Component Save for Schedule Rules
    public int save(ScheduleRule rule) {
        int id = 0;
        String saveRule = "";
        User user = rule.getUser();

        if (rule instanceof DailyReminder) {
            DailyReminder dailyRule = (DailyReminder) rule;
            saveRule = "INSERT INTO daily_reminders(user_id, message, time, days_left) "
                + "VALUES(?, ?, ?, ?)";
            
            try {
                PreparedStatement statement = connection.prepareStatement(saveRule, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, user.getID());
                statement.setString(2, dailyRule.getMessage());
                statement.setTimestamp(3, new Timestamp(dailyRule.getAlertTime().getTime()));
                statement.setInt(4, dailyRule.getDaysLeft());
                int rowsChanged = statement.executeUpdate();

                if (rowsChanged > 0) {
                    try {
                        ResultSet resultSet = statement.getGeneratedKeys();
    
                        if (resultSet.next()) {
                            id = resultSet.getInt(1);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (rule instanceof PeriodReminder) {
            PeriodReminder periodRule = (PeriodReminder) rule;
            saveRule = "INSERT INTO period_reminders(user_id, days_left, message, time, period, monthly) "
                + "VALUES(?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement statement = connection.prepareStatement(saveRule, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, user.getID());
                statement.setInt(2, periodRule.getDaysLeft());
                statement.setString(3, periodRule.getMessage());
                statement.setTimestamp(4, new Timestamp(periodRule.getAlertTime().getTime()));
                statement.setInt(5, periodRule.getPeriod());
                statement.setBoolean(6, periodRule.getMonthly());
                int rowsChanged = statement.executeUpdate();

                if (rowsChanged > 0) {
                    try {
                        ResultSet resultSet = statement.getGeneratedKeys();
    
                        if (resultSet.next()) {
                            id = resultSet.getInt(1);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Refresh rules
        getRules();

        return id;
    }

    // Get a user's schedule rules
    public ArrayList<ScheduleRule> getUserScheduleRules(User user) {
        ArrayList<ScheduleRule> userRules = new ArrayList<ScheduleRule>();

        for (ScheduleRule rule : rules) {
            if (rule.getUser().getID() == user.getID()) {
                userRules.add(rule);
            }
        }

        return userRules;
    }

    // Get a user based on ID
    public User getUserByID(int id) {
        User user = null;
        for (User u : users) {
            if (u.getID() == id) {
                user = u;
            }
        }

        return user;
    }
}
