package com.petervirtue;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.petervirtue.database.Database;
import com.petervirtue.models.DailyReminder;
import com.petervirtue.models.PeriodReminder;
import com.petervirtue.models.ScheduleRule;
import com.petervirtue.models.User;
import com.petervirtue.utils.DateTime;

public class App {

    // Default Database Information
    static final String url = "jdbc:mysql://localhost:3306/folia";
    static final String user = "root";
    static final String password = "";

    public static void main(String[] args) throws Exception {
        // Run the command line interface
        // Create the Database object 
        Database database = new Database(url, user, password);
        System.out.println("Folia Health Coding Challenge by Peter Virtue");
        run(database);
    }

    public static void run(Database database) {
        // Initialize command line prompts
        System.out.println("\nEnter the number of the option you would like to execute:\n1. Add a new user\n2. Get all users\n3. Get reminders for the day\n4. Add a new schedule rule\n\nOr type close to finish");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();

        if (option.equalsIgnoreCase("1")) {
            System.out.println("\nPlease enter the new user's name:");

            String name = scanner.nextLine();

            addUser(name, database);
            run(database);
        } else if (option.equalsIgnoreCase("2")) {
            ArrayList<User> users = database.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("There are no users currently.");
            } else {
                System.out.println("\nHere are all the current users (ID. Name):");

                for (User user : users) {
                    System.out.println(user.getID() + ". " + user.getName());
                }
            }

            run(database);
        } else if (option.equalsIgnoreCase("3")) {
            System.out.println("\nPlease enter the date you would like to check (mm/dd/yyyy OR today):");
            String dateString = scanner.nextLine();
            Date date = null;
            ArrayList<ScheduleRule> times = new ArrayList<ScheduleRule>();

            if (dateString.equalsIgnoreCase("today")) {
                date = new Date();
                times = getReminderTimesForDate(date, database);

                if (times.isEmpty()) {
                    System.out.println("\nThere are no reminders for today.");
                } else {
                    System.out.println("\nAll reminders for today:");
                    for (ScheduleRule time : times) {
                        System.out.println(time.getLocalizedReminder());
                    }
                }
            } else if (DateTime.getDateFromString(dateString) != null) {
                date = DateTime.getDateFromString(dateString);
                times = getReminderTimesForDate(date, database);

                if (times.isEmpty()) {
                    System.out.println("\nThere are no reminders for " + dateString + ".");
                } else {
                    System.out.println("\nAll reminders for " + dateString + ":");
                    for (ScheduleRule time : times) {
                        System.out.println(time.getLocalizedReminder());
                    }
                }
            } else {
                System.out.println("\nSorry, that isn't a valid date. Please try again.\n");
            }

            run(database);
        } else if (option.equalsIgnoreCase("4")) {
            System.out.println("\nEnter user ID for the reminder:");
            int userID = scanner.nextInt();
            scanner.nextLine();
            User user = database.getUserByID(userID);

            System.out.println("\nEnter the message for the reminder:");
            String message = scanner.nextLine();

            System.out.println("\nEnter the number of the option you would like to create:\n1. Daily\n2. Weekly\n3. Monthly\n4. Every N Days");
            String ruleType = scanner.nextLine();

            if (ruleType.equalsIgnoreCase("1")) {
                System.out.println("\nRemind every day indefinitely or for a certain amount of days [-1 for indefinite, number of days for limited]?");
                int daysLeft = scanner.nextInt();

                System.out.println("\nEnter the times you'd like to be reminded [HH:mm:ss, military time]. When you are done adding times, type \"done\":");
                ArrayList<Time> times = new ArrayList<Time>();

                String stringTime = scanner.nextLine();
                Time time = DateTime.getTimeFromString(stringTime);

                if (time != null) {
                    times.add(time);
                } 

                while (true) {
                    stringTime = scanner.nextLine();
                    time = DateTime.getTimeFromString(stringTime);

                    if (time != null) {
                        times.add(time);
                    } else {
                        break;
                    }
                }
                    
                for (Time t : times) {

                    DailyReminder reminder = new DailyReminder(user, message, t, daysLeft);
                    addScheduleRule(reminder, database);
                }

            } else if (ruleType.equalsIgnoreCase("2")) {
                int period = 6;

                System.out.println("\nWhat day of the week would you like for this reminder [Sunday 1, Monday 2, Tuesday 3, Wednesday 4, Thursday 5, Friday 6, Saturday 7]?");
                int daysLeft = DateTime.getDaysLeft(scanner.nextInt());
                scanner.nextLine();

                System.out.println("\nEnter the time you'd like to be reminded [HH:mm:ss, military time]:");
                String stringTime = scanner.nextLine();
                Time time = DateTime.getTimeFromString(stringTime);

                PeriodReminder reminder = new PeriodReminder(user, message, time, daysLeft, period, false);
                addScheduleRule(reminder, database);
            } else if (ruleType.equalsIgnoreCase("3")) {
                int daysLeft = 0;
                
                System.out.println("\nWhat day of the month would you like for this reminder [0 - 31, 29 - 31 will not remind for shorter months]?");
                int period = scanner.nextInt();
                scanner.nextLine();

                System.out.println("\nEnter the time you'd like to be reminded [HH:mm:ss, military time]:");
                String stringTime = scanner.nextLine();
                Time time = DateTime.getTimeFromString(stringTime);

                PeriodReminder reminder = new PeriodReminder(user, message, time, daysLeft, period, true);
                addScheduleRule(reminder, database);

            } else if (ruleType.equalsIgnoreCase("4")) {
                System.out.println("\nAfter how many days would you like to be reminded [number of days]?");
                int period = DateTime.getDaysLeft(scanner.nextInt());
                int daysLeft = period - 1;
                scanner.nextLine();

                System.out.println("\nEnter the time you'd like to be reminded [HH:mm:ss, military time]:");
                String stringTime = scanner.nextLine();
                Time time = DateTime.getTimeFromString(stringTime);

                PeriodReminder reminder = new PeriodReminder(user, message, time, daysLeft, period, false);
                addScheduleRule(reminder, database);
            } else {
                System.out.println("Sorry, that isn't an option. Please try again.\n");
            }

            run(database);
        } else if (!option.equalsIgnoreCase("close")) {
            System.out.println("Sorry, that isn't an option. Please try again.\n");
            run(database);
        }

        scanner.close();
    }

    public static void addUser(String name, Database database) {
        User user = new User(name);
        database.save(user);
        System.out.println("New user with name \"" + name + "\" has been saved to the database!");
    }

    // Main function number one, adding a schedule rule
    public static void addScheduleRule(ScheduleRule scheduleRule, Database database) {
        database.save(scheduleRule);
        System.out.println("New schedule rule with message \"" + scheduleRule.getMessage() + "\" has been saved to the database!");
    }

    public static ArrayList<ScheduleRule> getReminderTimesForDate(Date date, Database database) {
        ArrayList<ScheduleRule> rules = database.getAllScheduleRules();
        
        for (int i = 0; i < rules.size(); i++) {
            if (!rules.get(i).shouldAlertOnDay(date)) {
                rules.remove(i);
            }
        }

        return rules;
    }
}