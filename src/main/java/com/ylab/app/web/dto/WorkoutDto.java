package com.ylab.app.web.dto;

import com.ylab.app.model.user.User;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * WorkoutDto class represents a Data Transfer Object for workout-related information.
 * This class includes the workout ID, type, date, duration, calories burned, the user associated with the workout, and additional parameters.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDto {
    private Long id;
    private WorkoutType type;
    private LocalDateTime date;
    private int duration;
    private int caloriesBurned;
    private User user;
    private List<WorkoutAdditionalParams> params;
}