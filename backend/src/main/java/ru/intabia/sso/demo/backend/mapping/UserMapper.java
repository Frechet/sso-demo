package ru.intabia.sso.demo.backend.mapping;

import org.mapstruct.Mapper;
import ru.intabia.sso.demo.backend.dto.User;
import ru.intabia.sso.demo.backend.model.UserEntity;

/**
 * Mapper for User model and dto.
 */
@Mapper
public interface UserMapper {

    User mapToDto(UserEntity source);

    UserEntity mapFromDto(User source);
}
