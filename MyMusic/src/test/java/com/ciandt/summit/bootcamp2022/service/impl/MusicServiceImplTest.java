package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.dto.ArtistDTO;
import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Artist;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.exception.MusicNotFound;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import com.ciandt.summit.bootcamp2022.service.PlaylistService;
import com.ciandt.summit.bootcamp2022.service.mapper.ArtistDTOMapper;
import com.ciandt.summit.bootcamp2022.service.mapper.MusicDTOMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ContextConfiguration
class MusicServiceImplTest {

    @InjectMocks
    private MusicServiceImpl musicService;

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private PlaylistService playlistService;

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private MusicDTOMapper INSTANCE = MusicDTOMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        musicService = new MusicServiceImpl(musicRepository);
        mapStructSetup();
    }

    public void mapStructSetup() {
        ArtistDTOMapper artistDTOMapper = Mappers.getMapper(ArtistDTOMapper.class);
        ReflectionTestUtils.setField(INSTANCE, "artistDTOMapper", artistDTOMapper);
        musicService.setMusicDTOMapper(INSTANCE);
    }






    public Set<MusicDTO> buildMusicSetDto() {

        Set<MusicDTO> set = new HashSet();
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
        final Set<Music> music = new HashSet<>();
        final String filter = "sucesso";

        music.addAll(buildEntitySet("Paralamas", "do Sucesso"));
        music.addAll(buildEntitySet("Anitta", "com Sucesso"));

        given(musicRepository.findAllWithFilter(anyString(), any()))
                .willReturn(music);

        Set<MusicDTO> setAtual = musicService.findAllWithFilter(filter);

        Set<MusicDTO> setEsperado = INSTANCE.toSetOfDTO(music);

        assertEquals(setEsperado.size(), setAtual.size());
        assertEquals(setEsperado, setAtual);
    }

    @Test
    void whenNotFindAllWithFilterByName() {

        given(musicRepository.findAllWithFilter(anyString(), any()))
                .willReturn(Collections.emptySet());

        MusicNotFound thrown = assertThrows(
                MusicNotFound.class,
                () -> musicService.findAllWithFilter("ada"),
                "Exception not found"
        );

        assertEquals(thrown.getClass(), MusicNotFound.class);
    }
    @Test
    void insertANewMusicAtThePlaylist(){

        Set<Music> musicsAux = new HashSet<Music>();
        Music testMusic = new Music();
        testMusic.setName("I'm yours");
        musicsAux.add(testMusic);

        Playlist playlist = new Playlist();
        playlist.setId("1");
        playlist.setMusics(musicsAux);
        playlistRepository.save(playlist);

        MusicDTO musicDto = new MusicDTO();
        musicDto.setName("I'm yours");
        musicDto.setId("1");

       when(playlistService.addMusicToPlaylist(any(String.class),any(MusicDTO.class))).thenReturn(playlist);
       Playlist created = playlistService.addMusicToPlaylist("1", musicDto);
       assertThat(created.getMusics().iterator().next().getName()).isEqualTo(musicDto.getName());
    }
    private Music buildMusicEntity(String name, Artist a){
        Music m = new Music();
        m.setName(name);
        m.setId(UUID.nameUUIDFromBytes(name.getBytes()).toString());
        m.setArtist(a);
        return m;
    }

    public Set<Music> buildEntitySet(String artistName, String musicName) {
        Set<Music> set = new HashSet();
        Artist artist = new Artist();
        artist.setId(UUID.nameUUIDFromBytes(artistName.getBytes()).toString());
        artist.setName(artistName);

        Music music = buildMusicEntity(musicName, artist);
        set.add(music);

        return set;
    }
}