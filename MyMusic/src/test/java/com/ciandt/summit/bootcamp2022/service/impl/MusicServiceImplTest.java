package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.dto.ArtistDTO;
import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Artist;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.exception.MusicNotFound;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import com.ciandt.summit.bootcamp2022.service.mapper.ArtistDTOMapper;
import com.ciandt.summit.bootcamp2022.service.mapper.MusicDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ContextConfiguration
class MusicServiceImplTest {

    @InjectMocks
    private MusicServiceImpl musicService;

    @Mock
    private MusicRepository musicRepository;

    @InjectMocks
    private MusicDTOMapper INSTANCE = MusicDTOMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        musicService = new MusicServiceImpl(musicRepository);

    }

    @BeforeEach
    public void mapStructSetup() {
        INSTANCE = Mappers.getMapper(MusicDTOMapper.class);
        ArtistDTOMapper artistDTOMapper = Mappers.getMapper(ArtistDTOMapper.class);
        ReflectionTestUtils.setField(INSTANCE, "artistDTOMapper", artistDTOMapper);
    }


    public Set<Music> buildEntitySet(String artistName, String musicName) {
        Set<Music> set = new HashSet<>();
        Artist artist = new Artist();
        artist.setId(UUID.nameUUIDFromBytes(artistName.getBytes()).toString());
        artist.setName(artistName);

        Music music = buildMusicEntity(musicName, artist);
        set.add(music);

        return set;
    }

    private Music buildMusicEntity(String name, Artist a){
        Music m = new Music();
        m.setName(name);
        m.setId(UUID.nameUUIDFromBytes(name.getBytes()).toString());
        m.setArtist(a);
        return m;
    }

    public Set<MusicDTO> buildMusicSetDto() {

        Set<MusicDTO> set = new HashSet<>();
        ArtistDTO artist = new ArtistDTO();
        artist.setId(new UUID(3, 10).toString());
        artist.setName("Engenheiros");
        MusicDTO music = new MusicDTO();
        music.setId(new UUID(3, 10).toString());
        music.setName("Era Um Garoto Que Como Eu Amava Os Beatles");
        music.setArtist(artist);

        set.add(music);
        return set;

    }

    @Test
    void findAllWithFilter() {
        String filter = "public";
        musicService.setMusicDTOMapper(INSTANCE);

        Set<Music> music = new HashSet<>();
        music.addAll(buildEntitySet("Paralamas", "do Sucesso"));
        music.addAll(buildEntitySet("Anitta", "com Sucesso"));

        when(musicRepository.findAllWithFilter(anyString(), any())).thenReturn(music);


        Set<MusicDTO> setAtual = musicService.findAllWithFilter(filter);

        Set<MusicDTO> setEsperado = INSTANCE.toSetOfDTO(music);

        assertEquals(setEsperado.size(), setAtual.size());
        assertEquals(setEsperado, setAtual);
    }

    @Test
    void whenNotFindAllWithFilterByName() {
        musicService.setMusicDTOMapper(INSTANCE);

        when(musicRepository.findAllWithFilter(anyString(), any())).thenReturn(Collections.emptySet());

        MusicNotFound thrown = assertThrows(
                MusicNotFound.class,
                () -> musicService.findAllWithFilter("Sucesso"),
                "Exception not found"
        );

        assertEquals(thrown.getClass(), MusicNotFound.class);
    }


}