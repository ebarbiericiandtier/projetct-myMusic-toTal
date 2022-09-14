package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.exception.CannotModifyPlaylistException;
import com.ciandt.summit.bootcamp2022.exception.FreeAccountLimitException;
import com.ciandt.summit.bootcamp2022.exception.InvalidMusicException;
import com.ciandt.summit.bootcamp2022.exception.NotModified;
import com.ciandt.summit.bootcamp2022.exception.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import com.ciandt.summit.bootcamp2022.service.PlaylistService;
import com.ciandt.summit.bootcamp2022.service.UserService;
import com.ciandt.summit.bootcamp2022.service.mapper.PlaylistDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private static final Logger logger = LoggerFactory.getLogger(PlaylistServiceImpl.class);

    private final PlaylistRepository playlistRepository;
    private final MusicService musicService;
    @Autowired
    @Setter
    private PlaylistDTOMapper playlistDTOMapper;

    public Playlist findById(String id){

        return playlistRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid playlist id");
                    return new NoSuchElementException();
                });
    }

    @Override
    public Playlist findByUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playlistRepository.findById(user.getPlaylist().getId())
                .orElseThrow(PlaylistNotFoundException::new);
    }

    @Transactional
    public Playlist addMusicToPlaylist(String id, MusicDTO musicDTO){
        validatePlaylistOwner(id);

        final Music music = musicService.findById(musicDTO.getId());

        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Playlist was not found");
                    return new PlaylistNotFoundException();
                });

        if (playlistContainsMusic(playlist, music)) {
            throw new NotModified();
        }

        final int MAX_MUSIC_FREE_USER = 4;

        if (!isPremiumAccount() &&
                playlist.getMusics().size() == MAX_MUSIC_FREE_USER) {
            throw new FreeAccountLimitException();
        }

        playlist.getMusics().add(music);
        logger.info("Music added to playlist successfully");
        return playlistRepository.save(playlist);

    }

    @Transactional
    public Playlist removeMusicFromPlaylist(String id, MusicDTO musicDTO){

        final Playlist playlistFound = playlistRepository.findById(id)
                .orElseThrow(PlaylistNotFoundException::new);

        final Music musicInPlaylist = playlistFound.getMusicById(musicDTO.getId())
                .orElseThrow(() -> {
                    logger.error("Invalid music id");
                    return new InvalidMusicException();
                });

        logger.info("Music removed to playlist successfully");
        playlistFound.getMusics().remove(musicInPlaylist);
        return playlistRepository.save(playlistFound);
    }

    public boolean playlistContainsMusic(Playlist playlist, Music m){
        return playlist.getMusics().contains(m);
    }

    private boolean isPremiumAccount(){
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains("PREMIUM");
    }

    private void validatePlaylistOwner(String playlistIdParam){
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!u.getPlaylist().getId().equals(playlistIdParam)){
            throw new CannotModifyPlaylistException();
        }

    }

}
