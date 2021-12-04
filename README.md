# Folia Health Coding Challenge
## Intro
I have decided to implement the database component as well as the user interface component for this challenge in Java using the command line for user input. I created a MySQL database with three tables to manage Users, Daily Reminders and Reminders given out on different intervals (weekly, monthly, or every n days). Both of these reminders on the Java side are subclasses of the Reminder class which allows for checking notification dates generically. I am going to provide the documentation I have as well as a get started for this project, and if you have any questions feel free to reach out. Thank you again for this opportunity and I am looking forward to Monday!

## Get started
### MySQL
In the `database` folder, I have included a schema dump of the database that I used for this challenge. To get this going, start mysql (I did it on my local machine just on localhost:3306) and import the database. It should be named folia with the three tables mentioned above. If any of this information is different on your machine, simply edit the `url`, `sqluser` and `password` fields to match what you have setup on your local machine. 

### Java
The main class is App.java, and as long as all the other symbols are compiled running App.java should be successful. A couple of options to run this:
1. Run it using IntelliJ, Eclipse or VSCode to compile automatically
2. The commandline process:
- cd into the directory `folia-coding-challenge`
- run the following:
  - `/usr/bin/env /Library/Java/JavaVirtualMachines/adoptopenjdk-15.jdk/Contents/Home/bin/java -XX:+ShowCodeDetailsInExceptionMessages @/var/folders/lq/gfthhpmj6vdd399vpntvl7jh0000gn/T/cp_exy6lgn26e5c6odbitlz1jmma.argfile com.petervirtue.App`
  - Some of these arguments may need to be changed based on location of things, I also work on a Mac and these commands will not work on windows.

## Domain Model
#### Final Model
![Screen Shot 2021-12-03 at 7 06 14 PM](https://user-images.githubusercontent.com/14151831/144688270-4a0281fd-47c2-4a06-8849-5999a56f474b.png)

#### Planning Drawings & Iterations
[Folia Health Domain Model.pdf](https://github.com/petervirtue/folia-coding-challenge/files/7652981/Folia.Health.Domain.Model.pdf)

## Database Class Documentation
**Elements:**
- private Connection connection
- private ArrayList<User> users
- private ArrayList<ScheduleRule> rules

**Constructors:**
- Database(Strung url, String user, String password)
  
**Functions:**
- private void getUsers()
  - Used to perpetually update the users in the database instance
- private void getReminders()
  - Used to perpetually update the schedule reminders in the database instance
- public ArrayList<User> getAllUsers()
  - Gets all the users from the DB
- public ArrayList<Reminder> getAllReminders()
  - Gets all the reminders from the DB
- public int save(User)
  - Saves the provided user object to the database, and if it modififed a user record in the process it returns the id of the edited user
- public int save(Reminder)
  - Saves the provided reminder object to the database, and if it modififed a reminder record in the process it returns the id of the edited reminder
- public ArrayList<Reminder> getUserReminders(User)
  - Gets all the reminders from the DB for a specific user
- public User getUserByID(int)
  - Takes a user ID and returns the user object associated with that ID (null if user doesnt exist)
  


