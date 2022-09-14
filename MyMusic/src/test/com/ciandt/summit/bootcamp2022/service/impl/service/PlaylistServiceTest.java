package com.ciandt.summit.bootcamp2022.service.impl.service;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.dto.PlaylistDTO;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import com.ciandt.summit.bootcamp2022.service.impl.PlaylistServiceImpl;
import com.ciandt.summit.bootcamp2022.service.impl.builder.ArtistBuilder;
import com.ciandt.summit.bootcamp2022.service.impl.builder.MusicBuilder;
import com.ciandt.summit.bootcamp2022.service.impl.builder.PlaylistBuilder;
import com.ciandt.summit.bootcamp2022.service.mapper.ArtistDTOMapper;
import com.ciandt.summit.bootcamp2022.service.mapper.MusicDTOMapper;
import com.ciandt.summit.bootcamp2022.service.mapper.PlaylistDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class PlaylistServiceTest {


    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private MusicService musicService;

    @InjectMocks
    private PlaylistDTOMapper INSTANCE = PlaylistDTOMapper.INSTANCE;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        playlistService = new PlaylistServiceImpl(playlistRepository, musicService);
        mapperSetup();
    }

    void mapperSetup(){
        MusicDTOMapper musicDTOMapper = Mappers.getMapper(MusicDTOMapper.class);
        ReflectionTestUtils.setField(musicDTOMapper, "artistDTOMapper", Mappers.getMapper(ArtistDTOMapper.class));
        ReflectionTestUtils.setField(INSTANCE, "musicDTOMapper", musicDTOMapper);
        playlistService.setPlaylistDTOMapper(INSTANCE);

    }

   @Test
   void givenValidInputShouldReturnPlaylistWithNewMusic() throws Exception {
       final Set<MusicDTO> songs = new HashSet<>();
       songs.add(MusicBuilder.of(ArtistBuilder.simpleArtist("Necro"))
               .withMusicName( "vale do vale").build());
       songs.add(MusicBuilder.of(ArtistBuilder.simpleArtist("Tool"))
               .withMusicName("Schism").build());

       Playlist initialPlaylist = INSTANCE.toSetOfEntity(PlaylistBuilder.of(songs).build());

       final MusicDTO newSong = MusicBuilder.of(ArtistBuilder.simpleArtist("Tool"))
               .withMusicName("Imba").build();

       when(musicService.findById(ArgumentMatchers.anyString()))
               .thenReturn(MusicDTOMapper.INSTANCE.toEntity(newSong));

       when(playlistRepository.findById("123")).thenReturn(Optional.of(initialPlaylist));

       PlaylistDTO actual = playlistService.addMusicToPlaylist("123", newSong);

       PlaylistDTO expected = PlaylistBuilder.of(songs).build();
       expected.getMusics().add(newSong);
       assertEquals(actual, expected);

   }


}
