package com.ylab.app.web.mapper;

import com.ylab.app.model.workout.Workout;
import com.ylab.app.web.dto.WorkoutDto;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Interface for mapping workout entities to workout data transfer objects (DTO) and vice versa, using MapStruct.
 * This interface uses WorkoutAdditionalParamsMapper for additional parameters mapping.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@Mapper(componentModel = "spring", uses = {WorkoutAdditionalParamsMapper.class})
public interface WorkoutMapper {

    /**
     * Maps a workout entity to a workout data transfer object (DTO).
     *
     * @param workout the workout entity to map
     * @return the workout data transfer object
     */
    WorkoutDto workoutToWorkoutDto(Workout workout);

    /**
     * Converts a list of Workout entities to a list of WorkoutDto objects.
     *
     * @param list the list of Workout entities to be converted
     * @return the list of WorkoutDto objects
     */
    List<WorkoutDto> listWorkoutToWorkoutDto(List<Workout> list);

    /**
     * Maps a workout data transfer object (DTO) to a workout entity.
     *
     * @param workoutDto the workout data transfer object to map
     * @return the workout entity
     */
    Workout workoutDtoToWorkout(WorkoutDto workoutDto);
}