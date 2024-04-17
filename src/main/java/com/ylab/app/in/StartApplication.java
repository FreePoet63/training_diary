package com.ylab.app.in;

import com.ylab.app.model.audit.Audit;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.service.Audition;
import com.ylab.app.service.impl.UserServiceImpl;
import com.ylab.app.service.impl.WorkoutServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
    private static UserServiceImpl userService = new UserServiceImpl();
    private static WorkoutServiceImpl workoutService = new WorkoutServiceImpl();
    private static Audition audition = new Audition();
    private static User currentUser = null;

    /**
     * The entry point of the application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Register\n2. Login\n3. Exit");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    authorizeUser();
                    break;
                case "3":
                    System.out.println(audition.getAuditLogs());
                    System.exit(0);
                    break;
                default:
                    System.out.println("Wrong input. Please try again.");
            }
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
        UserRole role = UserRole.ADMIN;

        try {
            userService.registerUser(username, password, role);
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
            System.out.println("1. Add workout\n2. View workout\n3. Edit workout\n4. Delete workout\n5. Get calorie statistics\n6. Get statistics on parameters\n7. Logout");
            String input = scanner.nextLine();

            switch (input) {
                case "0":
                    if (currentUser.getRole().equals(UserRole.ADMIN)) {
                        viewAllWorkouts();
                    } else {
                        System.out.println("Wrong input. Please try again.");
                    }
                    break;
                case "1":
                    addWorkout();
                    break;
                case "2":
                    viewWorkouts();
                    break;
                case "3":
                    editWorkout();
                    break;
                case "4":
                    deleteWorkout();
                    break;
                case "5":
                    getStatistics();
                    break;
                case "6":
                    getStatisticsParams();
                    break;
                case "7":
                    currentUser = null;
                    System.out.println("You have left your account.");
                    break;
                default:
                    System.out.println("Wrong input. Please try again.");
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
        Map<String, Long> result = workoutService.getAdditionalParamsStats(currentUser, startTime, endTime);
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
            workoutService.addWorkout(workout);
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

        workoutService.editWorkout(workout, workoutId);
        audition.auditAction(currentUser, Audit.EDIT_WORKOUT);
        System.out.println("Workout successfully edition.");
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
        int caloriesBurned = getIntFromUser("Inject the amount of calories burned: ");
        List<WorkoutType> workoutTypes = getWorkoutTypesFromUser();
        List<WorkoutAdditionalParams> listParams = getWorkoutParamsFromUser();

        Workout newWorkout = new Workout();
        newWorkout.setId(existingWorkoutId != null ? existingWorkoutId : getLongFromUser("Enter ID workout:"));
        newWorkout.setDate(date);
        newWorkout.setDuration(duration);
        newWorkout.setCaloriesBurned(caloriesBurned);
        newWorkout.setWorkoutTypes(workoutTypes);
        newWorkout.setParams(listParams);
        newWorkout.setUser(currentUser);

        return newWorkout;
    }

    /**
     * Retrieves workout types data from the user for a new workout creation.
     *
     * @return a list of workout types
     */
    private static List<WorkoutType> getWorkoutTypesFromUser() {
        int sizeType = getIntFromUser("Enter number of workout types:");
        List<WorkoutType> workoutTypes = new ArrayList<>();
        for (int i = 0; i < sizeType; i++) {
            String type = getStringFromUser("Enter workout type " + (i + 1) + ":");
            workoutTypes.add(WorkoutType.add(type));
        }
        return workoutTypes;
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