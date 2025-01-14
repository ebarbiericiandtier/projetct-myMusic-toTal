package com.ciandt.summit.bootcamp2022.service.mapper;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Music;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring", uses = { ArtistDTOMapper.class } )
public interface MusicDTOMapper {

    MusicDTOMapper INSTANCE = Mappers.getMapper(MusicDTOMapper.class);
    @Mapping(source = "artist", target = "artist")
    MusicDTO toDto(Music music);

    @Mapping(source = "artist", target = "artist")
    Set<MusicDTO> toSetOfDTO(Set<Music> music);
}
