package com.ylab.app.model.workout;

import com.ylab.app.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class can be used to track user workouts, calculate fitness statistics,
 * and provide a historical record of the user's exercise routines.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workout {
    private Long id;
    private WorkoutType type;
    private LocalDateTime date;
    private int duration;
    private int caloriesBurned;
    private User user;
    private List<WorkoutAdditionalParams> params;
}