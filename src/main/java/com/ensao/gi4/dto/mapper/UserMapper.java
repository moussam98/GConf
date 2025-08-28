package com.ensao.gi4.dto.mapper;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS)
public interface UserMapper {
    UserDto toUserDto(User user);
    @InheritInverseConfiguration
    @Mapping(target = "password", ignore = true)
    User toUser(UserDto userDto);
}
