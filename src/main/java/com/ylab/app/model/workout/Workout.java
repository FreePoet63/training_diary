package com.ylab.app.model.workout;

import com.ylab.app.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Workout {
    private Long id;
    private LocalDateTime date;
    private int duration;
    private int caloriesBurned;
    private List<WorkoutType> workoutTypes;
    private List<WorkoutAdditionalParams> params;
    private User user;
}