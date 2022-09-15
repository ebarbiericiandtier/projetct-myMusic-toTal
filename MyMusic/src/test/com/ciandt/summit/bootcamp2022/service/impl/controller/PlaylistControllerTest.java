package com.ciandt.summit.bootcamp2022.service.impl.controller;

import com.ciandt.summit.bootcamp2022.controller.PlaylistController;
import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.dto.PlaylistDTO;
import com.ciandt.summit.bootcamp2022.exception.MusicNotFound;
import com.ciandt.summit.bootcamp2022.exception.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.service.impl.PlaylistServiceImpl;
import com.ciandt.summit.bootcamp2022.service.impl.builder.ArtistBuilder;
import com.ciandt.summit.bootcamp2022.service.impl.builder.MusicBuilder;
import com.ciandt.summit.bootcamp2022.service.impl.builder.PlaylistBuilder;
import com.ciandt.summit.bootcamp2022.service.impl.util.TestUtil;
import com.ciandt.summit.bootcamp2022.service.mapper.PlaylistDTOMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlaylistController.class)
@ContextConfiguration
@AutoConfigureMockMvc(addFilters = false)
public class PlaylistControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PlaylistServiceImpl playlistService;
    @MockBean
    PlaylistDTOMapper playlistDTOMapper;


    private final String API_PLAYLIST = "/api/v1";
    private final String PLAYLIST_ID = "12389381293sdasda121";


    @Test
    public void givenValidInputAddMusicToPlaylistShouldReturn200() throws Exception {
//         TODO :: Melhorar o Builder
//              :: Teste parametrizavel
        final Set<MusicDTO> musics = new HashSet<>();
        final MusicDTO m = MusicBuilder.of(ArtistBuilder.simpleArtist("Necro")).withMusicName( "vale do vale").build();
        final MusicDTO m2 = MusicBuilder.of(ArtistBuilder.simpleArtist("Tool")).withMusicName("Schism").build();
        musics.add(m);
        musics.add(m2);
        PlaylistDTO pl = PlaylistBuilder.of(musics).build();

        when(playlistService.addMusicToPlaylist(Mockito.anyString(), Mockito.any(MusicDTO.class))).thenReturn(pl);

        mockMvc.perform(post("/api/v1/playlists/12312312/musicas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(m)))
                .andExpect(jsonPath("$.musics", hasSize(2)))
                .andExpect(jsonPath("$.musics[0].name").value("Schism"))
                .andExpect(jsonPath("$.musics[1].name").value("vale do vale"))
                .andReturn();

    }


    @Test
    public void givenInvalidPlaylistAddMusicToPlaylistShouldReturn400() throws Exception {
        MusicDTO m = MusicBuilder.of(ArtistBuilder.simpleArtist("Necro")).withMusicName( "vale do vale").build();

        when(playlistService.addMusicToPlaylist(Mockito.anyString(), Mockito.any(MusicDTO.class))).thenThrow(PlaylistNotFoundException.class);

        mockMvc.perform(post("/api/v1/playlists/12312312/musicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(m)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    public void givenInvalidMusicAddMusicToPlaylistShouldReturn400() throws Exception {

        MusicDTO m = MusicBuilder.of(ArtistBuilder.simpleArtist("Necro")).withMusicName( "vale do vale").build();
        when(playlistService.addMusicToPlaylist(Mockito.anyString(), Mockito.any(MusicDTO.class))).thenThrow(MusicNotFound.class);

        mockMvc.perform(post("/api/v1/playlists/12312312/musicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(m)))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @Disabled
    public void givenMusicThatExistShouldReturn304()throws Exception {
        MusicBuilder.of(ArtistBuilder.simpleArtist("Bezoar"))
                .withMusicName( "Toke de somke").build();
    }


    @Test
    public void givenValidInputRemoveMusicFromPlaylistShouldReturn200()throws Exception {
        final Set<MusicDTO> musics = new HashSet<>();
        final MusicDTO m = MusicBuilder.of(ArtistBuilder.simpleArtist("Bezoar"))
                .withMusicName( "Toke de somke").build();

        musics.add(MusicBuilder.of(ArtistBuilder.simpleArtist("Tool"))
                .withMusicName("Schism").build());

        PlaylistDTO pl = PlaylistBuilder.of(musics).build();

        when(playlistService.removeMusicFromPlaylist(Mockito.anyString(), Mockito.any(MusicDTO.class))).thenReturn(pl);

        final String playlistId = pl.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/playlists/"+ playlistId +"/musicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(m)))
                .andExpect(jsonPath("$.musics", hasSize(1)))
                .andExpect(jsonPath("$.musics[0].name").value("Schism"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Disabled
    public void givenInvalidMusicRemoveMusicFromPlaylistShouldReturn400() throws Exception{
        throw new NoSuchMethodException();
    }

    @Test
    @Disabled
    public void givenInvalidPlaylistRemoveMusicFromPlaylistShouldReturn400() throws Exception{
        throw new NoSuchMethodException();
    }


}



