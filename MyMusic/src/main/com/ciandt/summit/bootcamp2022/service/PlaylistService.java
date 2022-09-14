package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Playlist;

public interface PlaylistService {
    PlaylistDTO addMusicToPlaylist(String id, MusicDTO musicDTO);

    PlaylistDTO removeMusicFromPlaylist(String playlistId, MusicDTO music);

    Playlist findById(String id);

    Playlist findByUser();
}
