package com.saptarshi.store.mappers;

import com.saptarshi.store.dto.UserDto;
import com.saptarshi.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
