package com.ylab.app.web.mapper;

import com.ylab.app.model.user.User;
import com.ylab.app.web.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * An interface for mapping user entities to user data transfer objects (DTO) and vice versa, using MapStruct.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Maps a user entity to a user data transfer object (DTO).
     *
     * @param user the user entity to map
     * @return the user data transfer object
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    UserDto userToUserDto(User user);

    /**
     * Maps a user data transfer object (DTO) to a user entity.
     *
     * @param userDto the user data transfer object to map
     * @return the user entity
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    User userDtoToUser(UserDto userDto);
}
