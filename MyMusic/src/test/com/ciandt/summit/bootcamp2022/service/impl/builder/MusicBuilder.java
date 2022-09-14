package com.ciandt.summit.bootcamp2022.service.impl.builder;

import com.ciandt.summit.bootcamp2022.dto.ArtistDTO;
import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.service.mapper.MusicDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class MusicBuilder {

    private String musicName;

    private ArtistDTO artistDTO;

    private MusicBuilder(){ }

    public MusicBuilder(String musicName){
        this.musicName = musicName;
    }

    public static MusicBuilder of(){
        return new MusicBuilder();
    }

    private MusicBuilder(ArtistDTO.ArtistDTOBuilder artistDTOBuilder){
        artistDTO = artistDTOBuilder.build();
    }

    public static MusicBuilder of(ArtistDTO.ArtistDTOBuilder artistDTOBuilder){
        return new MusicBuilder(artistDTOBuilder);
    }

    public String idGenerator(String source){
        return UUID.nameUUIDFromBytes(
                        source.getBytes())
                .toString();

    }
    public MusicBuilder withMusicName(String msc){
        this.musicName = msc;
        return this;
    }

    public MusicDTO build(){
        return MusicDTO.builder()
                .name(this.musicName)
                .artist(this.artistDTO)
                .id(idGenerator(this.musicName)).build();
    }


}