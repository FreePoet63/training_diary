package com.ylab.app.web.mapper;

import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.web.dto.WorkoutAdditionalParamsDto;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Interface for mapping workout additional parameters entities to workout additional parameters data transfer objects (DTO) and vice versa, using MapStruct.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@Mapper(componentModel = "spring")
public interface WorkoutAdditionalParamsMapper {

    /**
     * Maps workout additional parameters entity to workout additional parameters data transfer object (DTO).
     *
     * @param workoutAdditionalParams the workout additional parameters entity to map
     * @return the workout additional parameters data transfer object
     */
    WorkoutAdditionalParamsDto workoutAdditionalParamsToWorkoutAdditionalParamsDto(WorkoutAdditionalParams workoutAdditionalParams);

    /**
     * Converts a list of WorkoutAdditionalParams entities to a list of WorkoutAdditionalParamsDto objects.
     *
     * @param list the list of WorkoutAdditionalParams entities to be converted
     * @return the list of WorkoutAdditionalParamsDto objects
     */
    List<WorkoutAdditionalParamsDto> listWorkoutAdditionalParamsToWorkoutAdditionalParamsDto(List<WorkoutAdditionalParams> list);

    /**
     * Maps workout additional parameters data transfer object (DTO) to workout additional parameters entity.
     *
     * @param workoutAdditionalParamsDto the workout additional parameters data transfer object to map
     * @return the workout additional parameters entity
     */
    WorkoutAdditionalParams workoutAdditionalParamsDtoToWorkoutAdditionalParams(WorkoutAdditionalParamsDto workoutAdditionalParamsDto);
}