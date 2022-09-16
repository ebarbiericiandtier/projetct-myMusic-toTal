package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.dto.ArtistDTO;
import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.dto.PlaylistDTO;
import com.ciandt.summit.bootcamp2022.entity.Artist;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.enums.Role;
import com.ciandt.summit.bootcamp2022.exception.InvalidMusicException;
import com.ciandt.summit.bootcamp2022.exception.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.service.mapper.PlaylistDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class PlaylistServiceImplTest {
    @InjectMocks
    private MusicServiceImpl musicService;

    @Mock
    private MusicRepository musicRepository;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private PlaylistDTOMapper playlistDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        musicService = new MusicServiceImpl(musicRepository);
        playlistService = new PlaylistServiceImpl(playlistRepository, musicService);
        Mockito.when(playlistDTOMapper.toDto(Mockito.any(Playlist.class))).then(answer -> {
            Playlist playlist = answer.getArgument(0);
            Set<MusicDTO> musics = playlist.getMusics()
                                            .stream()
                                            .map(music -> {
                                                Artist artist = music.getArtist();
                                                ArtistDTO artistDTO = ArtistDTO.builder()
                                                                            .id(artist.getId())
                                                                            .name(artist.getName())
                                                                            .build();
                                                return MusicDTO.builder()
                                                        .id(music.getId())
                                                        .name(music.getName())
                                                        .artist(artistDTO)
                                                        .build();
                                            })
                                            .collect(Collectors.toSet());
            return PlaylistDTO.builder().id(playlist.getId()).musics(musics).build();
        });
    }

    @Test
    void findById() {
        Playlist playlist = new Playlist();
        playlist.setId(generateStringUUID());
        when(playlistRepository.findById(playlist.getId())).thenReturn(Optional.of(playlist));
        assertEquals(playlist,playlistService.findById(playlist.getId()));
    }

    @Test
    void removeMusicFromPlaylist() {

        Artist artist = new Artist();
        artist.setId(generateStringUUID());
        artist.setName("Rihanna");

        Music music1 = buildMusicEntity("Umbrella", artist);
        Music music2 = buildMusicEntity("Work", artist);
        Music music3 = buildMusicEntity("Diamonds", artist);

        Playlist playlist1 = new Playlist();
        playlist1.setId(generateStringUUID());

        User user = User.builder()
                        .id(new UUID(3, 10))
                        .username("José")
                        .playlist(playlist1)
                        .role(Role.COMUM)
                        .build();

        Set<Music> musics = new HashSet<>(Arrays.asList(music1, music2, music3));
        playlist1.setMusics(musics);

        //mock do security
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        when(playlistRepository.findAll()).thenReturn(Collections.singletonList(playlist1));
        when(playlistRepository.findById(anyString())).thenReturn(Optional.of(playlist1));


        when(playlistRepository.save(playlist1)).thenReturn(playlist1);

        when(musicRepository.findById(music1.getId())).thenReturn(Optional.of(music1));
        when(musicRepository.findById(music2.getId())).thenReturn(Optional.of(music2));
        when(musicRepository.findById(music3.getId())).thenReturn(Optional.of(music3));

        MusicDTO music1DTO = new MusicDTO();
        music1DTO.setId(music1.getId());

        PlaylistDTO playlistAfterRemove = playlistService.removeMusicFromPlaylist(playlist1.getId(), music1DTO);

        assertFalse(playlistAfterRemove.getMusics().contains(music1));
        assertTrue(playlistAfterRemove.getMusics().containsAll(Arrays.asList(music2, music3)));

    }

    @Test
    void removeMusicFromInvalidPlaylistThrowsException() {

        Artist artist = new Artist();
        artist.setId(generateStringUUID());
        artist.setName("Rihanna");

        Music music1 = buildMusicEntity("Umbrella", artist);

        MusicDTO music1DTO = new MusicDTO();
        music1DTO.setId(music1.getId());

        assertThrows(PlaylistNotFoundException.class, () -> playlistService.removeMusicFromPlaylist("123", music1DTO));

    }

    @Test
    void removeInvalidMusicFromPlaylistThrowsException() {

        Artist artist = new Artist();
        artist.setId(generateStringUUID());
        artist.setName("Rihanna");

        Music music2 = buildMusicEntity("Work", artist);
        Music music3 = buildMusicEntity("Diamonds", artist);

        Playlist playlist1 = new Playlist();
        playlist1.setId(generateStringUUID());

        Set<Music> musics = new HashSet<>(Arrays.asList(music2, music3));
        playlist1.setMusics(musics);

        when(playlistRepository.findAll()).thenReturn(Collections.singletonList(playlist1));
        when(playlistRepository.findById(anyString())).thenReturn(Optional.of(playlist1));


        when(playlistRepository.save(playlist1)).thenReturn(playlist1);
        when(musicRepository.findById(music2.getId())).thenReturn(Optional.of(music2));
        when(musicRepository.findById(music3.getId())).thenReturn(Optional.of(music3));

        MusicDTO music1DTO = new MusicDTO();
        music1DTO.setId("122");

        assertThrows(InvalidMusicException.class, () -> playlistService.removeMusicFromPlaylist(playlist1.getId(), music1DTO));
    }

    private Music buildMusicEntity(String name, Artist a){
        Music m = new Music();
        m.setName(name);
        m.setId(generateStringUUID());
        m.setArtist(a);
        return m;
    }

    private String generateStringUUID() {
        return new UUID(3, 10).toString();
    }
}