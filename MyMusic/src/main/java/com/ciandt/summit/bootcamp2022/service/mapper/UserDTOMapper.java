package com.ciandt.summit.bootcamp2022.service.mapper;

import com.ciandt.summit.bootcamp2022.dto.UserDto;
import com.ciandt.summit.bootcamp2022.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel              = "spring",
        uses                        = { UserDTOMapper.class }
)
public interface UserDTOMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);
}
