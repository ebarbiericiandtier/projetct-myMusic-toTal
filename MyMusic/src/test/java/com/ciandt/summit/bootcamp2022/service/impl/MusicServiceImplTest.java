package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.dto.ArtistDTO;
import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Artist;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import com.ciandt.summit.bootcamp2022.service.mapper.ArtistDTOMapper;
import com.ciandt.summit.bootcamp2022.service.mapper.MusicDTOMapper;
import com.ciandt.summit.bootcamp2022.service.mapper.MusicDTOMapperImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MusicServiceImplTest {

    @InjectMocks
    private MusicServiceImpl musicService;

    @Mock
    private MusicRepository musicRepository;

    private MusicDTOMapper musicDTOMapper = MusicDTOMapperImpl.INSTANCE;

    //@Spy
    //private MusicDTOMapper musicDTOMapper = Mappers.getMapper(MusicDTOMapper.class);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        musicService = new MusicServiceImpl(musicRepository);
    }

    public void init() {
        musicDTOMapper = Mappers.getMapper(MusicDTOMapper.class);
        ArtistDTOMapper artistDTOMapper = Mappers.getMapper(ArtistDTOMapper.class);
        ReflectionTestUtils.setField(musicDTOMapper, "musicDTOMapper", artistDTOMapper);
    }

    public Set<Music> buildMusicSet() {

        Set<Music> set = new HashSet();
        Artist artist = new Artist();
        artist.setId(new UUID(3, 10));
        artist.setName("Engenheiros");
        Music music = new Music();
        music.setId(new UUID(3, 10));
        music.setName("Era Um Garoto Que Como Eu Amava Os Beatles");
        music.setArtist(artist);

        set.add(music);
        return set;

    }

    public Set<MusicDTO> buildMusicSetDto() {

        Set<MusicDTO> set = new HashSet();
        ArtistDTO artist = new ArtistDTO();
        artist.setId(new UUID(3, 10));
        artist.setName("Engenheiros");
        MusicDTO music = new MusicDTO();
        music.setId(new UUID(3, 10));
        music.setName("Era Um Garoto Que Como Eu Amava Os Beatles");
        music.setArtist(artist);

        set.add(music);
        return set;

    }

    @Test
    void findAllWithFilter() {
        String filter = "public";

        Sort sort = Sort.by("artist.name").ascending()
                .and(Sort.by("name").ascending());

        Set<Music> music = buildMusicSet();

        when(musicRepository.findAllWithFilter(filter, sort)).thenReturn(music);

        musicService.setMusicDTOMapper(musicDTOMapper);

        Set<MusicDTO> setAtual = musicService.findAllWithFilter(filter);

        Set<MusicDTO> setEsperado = musicDTOMapper.toSetOfDTO(music);

        assertEquals(setEsperado, setAtual);
    }

    @Test
    void whenNotFindAllWithFilterByName() {

    }


}