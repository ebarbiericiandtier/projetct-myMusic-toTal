package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.dto.PlaylistDTO;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.enums.Role;
import com.ciandt.summit.bootcamp2022.exception.CannotModifyPlaylistException;
import com.ciandt.summit.bootcamp2022.exception.FreeAccountLimitException;
import com.ciandt.summit.bootcamp2022.exception.InvalidMusicException;
import com.ciandt.summit.bootcamp2022.exception.NotModified;
import com.ciandt.summit.bootcamp2022.exception.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import com.ciandt.summit.bootcamp2022.service.PlaylistService;
import com.ciandt.summit.bootcamp2022.service.mapper.PlaylistDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PlaylistDTO addMusicToPlaylist(String id, MusicDTO musicDTO){

        final Music music = musicService.findById(musicDTO.getId())
                .orElseThrow(InvalidMusicException::new);

        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Playlist was not found");
                    return new PlaylistNotFoundException();
                });

        validatePlaylistOwner(id);

        if (playlistContainsMusic(playlist, music)) {
            throw new NotModified();
        }

        final int MAX_MUSIC_FREE_USER = 5;

        if (!isPremiumAccount() &&
                playlist.getMusics().size() == MAX_MUSIC_FREE_USER) {
            throw new FreeAccountLimitException();
        }

        playlist.getMusics().add(music);
        logger.info("Music added to playlist successfully");
        playlistRepository.save(playlist);
        return playlistDTOMapper.toDto(playlist);

    }

    @Transactional
    public PlaylistDTO removeMusicFromPlaylist(String id, MusicDTO musicDTO){

        final Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(PlaylistNotFoundException::new);

        validatePlaylistOwner(id);

        final Music music = musicService.findById(musicDTO.getId())
                .orElseThrow(InvalidMusicException::new);

        if (!playlistContainsMusic(playlist, music)) {
            logger.error("Song doesnot exists in playlist");
            throw new NotModified();
        }

        logger.info("Music removed to playlist successfully");
        playlist.getMusics().remove(music);
        playlistRepository.save(playlist);
        return playlistDTOMapper.toDto(playlist);
    }

    public boolean playlistContainsMusic(Playlist playlist, Music m){
        return playlist.getMusics().contains(m);
    }

    public boolean isPremiumAccount(){
        User u  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return u.getRole().equals(Role.PREMIUM);
    }

    public void validatePlaylistOwner(String playlistIdParam){
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!u.getPlaylist().getId().equals(playlistIdParam)){
            throw new CannotModifyPlaylistException();
        }
    }

}
