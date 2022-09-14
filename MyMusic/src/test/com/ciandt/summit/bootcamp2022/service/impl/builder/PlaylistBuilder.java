package com.ciandt.summit.bootcamp2022.service.impl.builder;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.dto.PlaylistDTO;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlaylistBuilder {
    private String id = idGenerator("Simple id generat");
    private Set<MusicDTO> musics = new HashSet<>();


    private PlaylistBuilder() { }
    private PlaylistBuilder(Set<MusicDTO> musics){
        this.musics = musics;
    }

    public static PlaylistBuilder of(Set<MusicDTO> musics){
        return new PlaylistBuilder(musics);
    }

    public static PlaylistBuilder empty(){
       return new PlaylistBuilder();
    }

    public PlaylistDTO build(){
        return PlaylistDTO.builder()
                .id(this.id)
                .musics(musics)
                .build();
    }

    private String idGenerator(String source){
        return UUID.nameUUIDFromBytes(
                        source.getBytes())
                .toString();

    }
}
