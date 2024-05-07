package com.ylab.app.web.mapper;

import com.ylab.app.model.workout.Workout;
import com.ylab.app.web.dto.WorkoutDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Interface for mapping workout entities to workout data transfer objects (DTO) and vice versa, using MapStruct.
 * This interface uses WorkoutAdditionalParamsMapper for additional parameters mapping.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@Mapper(uses = {WorkoutAdditionalParamsMapper.class})
public interface WorkoutMapper {
    WorkoutMapper INSTANCE = Mappers.getMapper(WorkoutMapper.class);

    /**
     * Maps a workout entity to a workout data transfer object (DTO).
     *
     * @param workout the workout entity to map
     * @return the workout data transfer object
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "type", source = "type"),
            @Mapping(target = "date", source = "date"),
            @Mapping(target = "duration", source = "duration"),
            @Mapping(target = "caloriesBurned", source = "caloriesBurned"),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "params", source = "params")
    })
    WorkoutDto workoutToWorkoutDto(Workout workout);

    /**
     * Maps a workout data transfer object (DTO) to a workout entity.
     *
     * @param workoutDto the workout data transfer object to map
     * @return the workout entity
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "type", source = "type"),
            @Mapping(target = "date", source = "date"),
            @Mapping(target = "duration", source = "duration"),
            @Mapping(target = "caloriesBurned", source = "caloriesBurned"),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "params", source = "params")
    })
    Workout workoutDtoToWorkout(WorkoutDto workoutDto);
}