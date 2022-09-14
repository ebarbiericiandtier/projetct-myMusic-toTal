package com.ciandt.summit.bootcamp2022.service.mapper;

import com.ciandt.summit.bootcamp2022.dto.PlaylistDTO;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel              = "spring",
        uses                        = { MusicDTOMapper.class },
        collectionMappingStrategy   = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface PlaylistDTOMapper {

    PlaylistDTOMapper INSTANCE = Mappers.getMapper(PlaylistDTOMapper.class);

    PlaylistDTO toDto(Playlist playlist);

    Playlist toEntity(PlaylistDTO dto);

    Playlist toSetOfEntity(PlaylistDTO dto);
}