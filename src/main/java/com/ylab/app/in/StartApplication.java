package com.ylab.app.in;

import com.ylab.app.dbService.migration.LiquibaseMigration;
import com.ylab.app.model.audit.Audit;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.service.Audition;
import com.ylab.app.service.UserService;
import com.ylab.app.service.WorkoutService;
import com.ylab.app.service.impl.UserServiceImpl;
import com.ylab.app.service.impl.WorkoutServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.ylab.app.util.DataResultDatabase.*;
import static com.ylab.app.util.DataResultDatabase.getPassword;

/**
 * StartApplication class
 *
 * Entry point for the application providing user registration, login, and workout management functionalities.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public class StartApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LiquibaseMigration liquibaseMigration = new LiquibaseMigration();
    private static final UserService userService = new UserServiceImpl();
    private static final WorkoutService workoutService = new WorkoutServiceImpl();
    private static final Audition audition = new Audition();
    private static User currentUser = null;

    /**
     * The entry point of the application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        liquibaseMigration.performLiquibaseMigration(getUrl() + getDatabaseName(), getUsername(), getPassword());
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            String input = scanner.nextLine();

            String result = switch (input) {
                case "1" -> {
                    registerUser();
                    yield "User registered.";
                }
                case "2" -> {
                    authorizeUser();
                    yield "User authorized.";
                }
                case "3" -> {
                    System.out.println(audition.getAuditLogs());
                    System.exit(0);
                    yield "Exiting program.";
                }
                default -> "Wrong input. Please try again.";
            };

            System.out.println(result);
        }
    }

    /**
     * Handles the user registration process.
     */
    private static void registerUser() {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        try {
            userService.registerUser(username, password, UserRole.USER);
            System.out.println("User successfully registered.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Manages the user login process.
     */
    private static void authorizeUser() {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        try {
            currentUser = userService.loginUser(username, password);
            System.out.println("Authentication is successful. Welcome, " + username + "!");
            audition.auditAction(currentUser, Audit.LOGIN);
            userMenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Menu for user actions based on their role.
     */
    private static void userMenu() {
        while (currentUser != null) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                System.out.println("0. View all training");
            }
            System.out.println("1. Add workouts");
            System.out.println("2. View workout");
            System.out.println("3. Edit workout");
            System.out.println("4. Delete workout");
            System.out.println("5. Get calorie statistics");
            System.out.println("6. Get statistics on parameters");
            System.out.println("7. Logout");
            String input = scanner.nextLine();

            switch (input) {
                case "0" -> {
                    if (currentUser.getRole().equals(UserRole.ADMIN)) {
                        viewAllWorkouts();
                    } else {
                        System.out.println("Wrong input. Please try again.");
                    }
                }
                case "1" -> addWorkout();
                case "2" -> viewWorkouts();
                case "3" -> editWorkout();
                case "4" -> deleteWorkout();
                case "5" -> getStatistics();
                case "6" -> getStatisticsParams();
                case "7" -> {
                    currentUser = null;
                    System.out.println("You have left your account.");
                }
                default -> System.out.println("Wrong input. Please try again.");
            }
        }
    }

    /**
     * Displays all workouts for the admin user.
     */
    private static void viewAllWorkouts() {
        System.out.println(workoutService.getAllReadingsWorkouts(currentUser));
    }

    /**
     * Retrieves and displays statistics based on additional workout parameters for a given time frame.
     */
    private static void getStatisticsParams() {
        LocalDateTime startTime = getDateFromUser("Enter start date ");
        LocalDateTime endTime = getDateFromUser("Enter end date ");
        WorkoutType type = getWorkoutTypeFromUser();
        List<WorkoutAdditionalParams> result = workoutService.getAdditionalParamsStats(currentUser, type, startTime, endTime);
        System.out.println(currentUser + " " + result);
        audition.auditAction(currentUser, Audit.STATISTICS_PARAMS);
    }

    /**
     * Retrieves and displays statistics on calories burned by the user within a specified time period.
     */
    private static void getStatistics() {
        LocalDateTime startTime = getDateFromUser("Enter start date ");
        LocalDateTime endTime = getDateFromUser("Enter end date ");
        int resultStatistics = workoutService.getCaloriesBurnedInTimePeriod(currentUser, startTime, endTime);
        System.out.println(resultStatistics);
        audition.auditAction(currentUser, Audit.STATISTICS_CALORIES);
    }

    /**
     * Handles the addition of a new workout by the user with necessary input validations and error handling.
     */
    private static void addWorkout() {
        try {
            Workout workout = createWorkoutFromUserInput(null);
            workoutService.addWorkout(currentUser, workout);
            System.out.println("Workout added successfully:");
            System.out.println(workout);
            audition.auditAction(currentUser, Audit.ADD_WORKOUT);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid input format.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Handles the editing of an existing workout based on user input.
     */
    private static void editWorkout() {
        System.out.println("Enter training ID to edit:");
        Long workoutId = Long.parseLong(scanner.nextLine());

        Workout workout = createWorkoutFromUserInput(workoutId);

        workoutService.editWorkout(currentUser, workout, workoutId);
        audition.auditAction(currentUser, Audit.EDIT_WORKOUT);
        System.out.println("Workout successfully edited.");
    }

    /**
     * Displays the workouts scheduled for a specific date based on user input.
     */
    private static void viewWorkouts() {
        LocalDateTime targetDate = getDateFromUser("Enter data: ");
        List<Workout> workouts = workoutService.getWorkoutsOnDate(currentUser, targetDate);
        workouts.forEach(System.out::println);
        audition.auditAction(currentUser, Audit.WORKOUTS_ON_DATE);
    }

    /**
     * Handles the deletion of a specific workout based on user input.
     */
    private static void deleteWorkout() {
        System.out.println("Enter training ID to delete:");
        Long workoutId = Long.parseLong(scanner.nextLine());
        workoutService.deleteWorkout(workoutId);
        audition.auditAction(currentUser, Audit.DELETE_WORKOUT);
    }

    /**
     * Creates a new workout object based on user input data and parameters.
     *
     * @param existingWorkoutId ID of the existing workout if any for editing, null if adding a new workout
     * @return the created workout object
     */
    private static Workout createWorkoutFromUserInput(Long existingWorkoutId) {
        LocalDateTime date = LocalDateTime.now();
        int duration = getIntFromUser("Enter duration: ");
        int caloriesBurned = getIntFromUser("Enter the amount of calories burned: ");
        WorkoutType workoutType = getWorkoutTypeFromUser();

        List<WorkoutAdditionalParams> listParams = getWorkoutParamsFromUser();

        Workout newWorkout = new Workout();
        if (existingWorkoutId != null) {
            newWorkout.setId(existingWorkoutId);
        }
        newWorkout.setType(workoutType);
        newWorkout.setDate(date);
        newWorkout.setDuration(duration);
        newWorkout.setCaloriesBurned(caloriesBurned);
        newWorkout.setParams(listParams);
        newWorkout.setUser(currentUser);

        return newWorkout;
    }

    /**
     * Retrieves workout types data from the user for a new workout creation.
     *
     * @return of workout types
     */
    private static WorkoutType getWorkoutTypeFromUser() {
        System.out.println("Available workout types:");
        for (WorkoutType type : WorkoutType.values()) {
            System.out.println((type.ordinal() + 1) + ". " + type);
        }

        int typeIndex = getIntFromUser("Enter the number for the workout type:") - 1;
        if (typeIndex >= 0 && typeIndex < WorkoutType.values().length) {
            return WorkoutType.values()[typeIndex];
        } else {
            System.out.println("Invalid number. Please try again.");
            return getWorkoutTypeFromUser();
        }
    }

    /**
     * Retrieves additional parameters data from the user for a new workout creation.
     *
     * @return a list of additional workout parameters
     */
    private static List<WorkoutAdditionalParams> getWorkoutParamsFromUser() {
        int sizeParams = getIntFromUser("Enter number of additional parameters:");
        List<WorkoutAdditionalParams> listParams = new ArrayList<>();
        for (int i = 0; i < sizeParams; i++) {
            String param = getStringFromUser("Enter additional parameter " + (i + 1) + ":");
            Long value = getLongFromUser("Enter value for " + param + ":");
            listParams.add(WorkoutAdditionalParams.add(param, value));
        }
        return listParams;
    }

    /**
     * Retrieves date and time information from the user based on the given message.
     *
     * @param message the message prompting user input
     * @return the LocalDateTime object representing the selected date and time
     */
    private static LocalDateTime getDateFromUser(String message) {
        System.out.println(message);
        int year = getIntFromUser("Enter year:");
        int month = getIntFromUser("Enter number month:");
        int day = getIntFromUser("Enter day month:");
        return LocalDateTime.of(year, month, day, 0, 0);
    }

    /**
     * Retrieves an integer value from the user based on the given message.
     *
     * @param message the message prompting user input
     * @return the integer input value
     */
    private static int getIntFromUser(String message) {
        System.out.println(message);
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Retrieves a string value from the user based on the given message.
     *
     * @param message the message prompting user input
     * @return the string input value
     */
    private static String getStringFromUser(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Retrieves a long value from the user based on the given message.
     *
     * @param message the message prompting user input
     * @return the long input value
     */
    private static long getLongFromUser(String message) {
        System.out.print(message);
        return Long.parseLong(scanner.nextLine());
    }
}