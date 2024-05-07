package com.ylab.app.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ylab.app.model.workout.WorkoutType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
@Schema(description = "Workout DTO")
public class WorkoutDto {
    @Schema(description = "Workout Id", example = "1")
    @NotNull(message = "Id must be not null.")
    private Long id;

    @Schema(description = "Workout type", example = "CROSSFIT")
    @NotNull(message = "Workout type must be not null.")
    private WorkoutType type;

    @Schema(description = "DateTime of workout", type="string", example = "2018-01-01 22:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    @Schema(description = "Workout duration", example = "100")
    @NotNull(message = "Duration must be not null.")
    private Integer duration;

    @Schema(description = "Total calories", example = "250")
    @NotNull(message = "Calories burned must be not null.")
    private Integer caloriesBurned;

    private List<WorkoutAdditionalParamsDto> params;
}