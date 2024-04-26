package com.ylab.app.web.mapper;

import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.web.dto.WorkoutAdditionalParamsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Interface for mapping workout additional parameters entities to workout additional parameters data transfer objects (DTO) and vice versa, using MapStruct.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@Mapper
public interface WorkoutAdditionalParamsMapper {
    WorkoutAdditionalParamsMapper INSTANCE = Mappers.getMapper(WorkoutAdditionalParamsMapper.class);

    /**
     * Maps workout additional parameters entity to workout additional parameters data transfer object (DTO).
     *
     * @param workoutAdditionalParams the workout additional parameters entity to map
     * @return the workout additional parameters data transfer object
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "params", source = "params"),
            @Mapping(target = "value", source = "value")
    })
    WorkoutAdditionalParamsDto workoutAdditionalParamsToWorkoutAdditionalParamsDto(WorkoutAdditionalParams workoutAdditionalParams);

    /**
     * Maps workout additional parameters data transfer object (DTO) to workout additional parameters entity.
     *
     * @param workoutAdditionalParamsDto the workout additional parameters data transfer object to map
     * @return the workout additional parameters entity
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "params", source = "params"),
            @Mapping(target = "value", source = "value")
    })
    WorkoutAdditionalParams workoutAdditionalParamsDtoToWorkoutAdditionalParams(WorkoutAdditionalParamsDto workoutAdditionalParamsDto);
}