package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.exception.InvalidMusicException;
import com.ciandt.summit.bootcamp2022.exception.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import com.ciandt.summit.bootcamp2022.service.PlaylistService;
import com.ciandt.summit.bootcamp2022.service.mapper.PlaylistDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MusicService musicService;
    @Autowired
    @Setter
    private PlaylistDTOMapper playlistDTOMapper;

    public Playlist findById(String id){
        return playlistRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Playlist addMusicToPlaylist(String id, MusicDTO musicDTO){

    }

    @Transactional
    public Playlist removeMusicFromPlaylist(String id, MusicDTO musicDTO){

        final Playlist playlistFound = playlistRepository.findById(id)
                .orElseThrow(PlaylistNotFoundException::new);

        final Music musicInPlaylist = playlistFound.getMusicById(musicDTO.getId())
                .orElseThrow(InvalidMusicException::new);

        playlistFound.getMusics().remove(musicInPlaylist);
        return playlistRepository.save(playlistFound);
    }

}
