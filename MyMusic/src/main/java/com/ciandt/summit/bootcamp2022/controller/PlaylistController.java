package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistRepository playlistRepository;
    private final PlaylistService playlistService;


    @GetMapping("/{id}")
    ResponseEntity<Playlist> findById(@PathVariable("id") @NotNull String id){
        Playlist playlist = playlistRepository
                .findAll().stream().findAny()
                .orElseThrow(IllegalAccessError::new);

        return ResponseEntity.ok(playlist);
    }

    @PostMapping("/{playlistId}/musics")
    ResponseEntity<Playlist> addMusicToPlaylist(
            @PathVariable("playlistId") String playlistId,
            @Valid @RequestBody MusicDTO musics){
        return ResponseEntity.ok(
                playlistService.addMusicToPlaylist(playlistId, musics));
    }




}
