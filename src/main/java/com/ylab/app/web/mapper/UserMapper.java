package com.ylab.app.web.mapper;

import com.ylab.app.model.user.User;
import com.ylab.app.web.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * An interface for mapping user entities to user data transfer objects (DTO) and vice versa, using MapStruct.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a user entity to a user data transfer object (DTO).
     *
     * @param user the user entity to map
     * @return the user data transfer object
     */
    UserDto userToUserDto(User user);

    /**
     * Converts a list of User entities to a list of UserDto objects.
     *
     * @param users the list of User entities to be converted
     * @return the list of UserDto objects
     */
    List<UserDto> listUserToUserDto(List<User> users);

    /**
     * Maps a user data transfer object (DTO) to a user entity.
     *
     * @param userDto the user data transfer object to map
     * @return the user entity
     */
    User userDtoToUser(UserDto userDto);
}
