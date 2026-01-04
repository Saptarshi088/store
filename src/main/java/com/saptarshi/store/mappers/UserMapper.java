package com.saptarshi.store.mappers;

import com.saptarshi.store.dto.UpdateUserRequest;
import com.saptarshi.store.dto.UserDto;
import com.saptarshi.store.dto.UserRegisterReqiest;
import com.saptarshi.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toUser(UserRegisterReqiest request);

    void toUpdate(UpdateUserRequest request, @MappingTarget User user);
}
