package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.dto.PlaylistDTO;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.service.PlaylistService;
import com.ciandt.summit.bootcamp2022.service.mapper.PlaylistDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;
    private final PlaylistDTOMapper playlistDTOMapper;

    @GetMapping
    ResponseEntity<Playlist> findByUser(){
       return ResponseEntity.ok(
               playlistService.findByUser()
       );
    }


    @Operation(summary = "Search for playlist based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns playlist", content =
                    {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PlaylistDTO.class))}),
            @ApiResponse(responseCode = "204", description = "Playlist wasnt found"),
    })
    @GetMapping("/{id}")
    ResponseEntity<PlaylistDTO> findById(@PathVariable("id") String id){
       PlaylistDTO playlistDTO =
               playlistDTOMapper.toDto(playlistService.findById(id));
        return ResponseEntity.ok(
                playlistDTO
        );
    }

    @Operation(summary = "Insert a Music in Playlist based on their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns playlist with updated songs", content =
                    {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PlaylistDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameter for music or playlist", content = @Content),
            @ApiResponse(responseCode = "304", description = "Music already exists in Playlist", content = @Content)
    })
    @PostMapping("/{playlistId}/musicas")
    ResponseEntity<Playlist> addMusicToPlaylist(
            @PathVariable("playlistId") String playlistId,
            @Valid @RequestBody MusicDTO music){
        return ResponseEntity.ok(
                playlistService.addMusicToPlaylist(playlistId, music));
    }
    @Operation(summary = "Delete a Music from Playlist based on their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a playlist with updated songs",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlaylistDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameter for music or playlist", content = @Content)
    })
    @DeleteMapping("/{playlistId}/musicas")
    ResponseEntity<PlaylistDTO> removeMusicFromPlaylist(
            @PathVariable("playlistId") String playlistId,
            @Valid @RequestBody MusicDTO musicDTO){

        Playlist playlist =
                playlistService.removeMusicFromPlaylist(playlistId, musicDTO);

        return ResponseEntity.ok(
                playlistDTOMapper.toDto(playlist)
        );
    }



}
