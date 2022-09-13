package com.ciandt.summit.bootcamp2022.service.mapper;


import com.ciandt.summit.bootcamp2022.dto.UserDTO;
import com.ciandt.summit.bootcamp2022.entity.User;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel              = "spring",
        uses                        = { UserDTOMapper.class },
        collectionMappingStrategy   = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface UserDTOMapper {

    UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

    UserDTO toDto(User user);

}
