package com.ciandt.summit.bootcamp2022.service.impl.builder;

import com.ciandt.summit.bootcamp2022.dto.ArtistDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class ArtistBuilder {
    private String artistName;
    private String id;
    private ArtistBuilder(){}


    public static ArtistBuilder of(){
        return new ArtistBuilder();
    }

    public static ArtistDTO.ArtistDTOBuilder simpleArtist(String artist){
        return ArtistDTO.builder()
                .id(of().idGenerator("name artist rdm"))
                .name(artist);
    }


    private String idGenerator(String source){
        return UUID.nameUUIDFromBytes(
                        source.getBytes())
                .toString();

    }

}