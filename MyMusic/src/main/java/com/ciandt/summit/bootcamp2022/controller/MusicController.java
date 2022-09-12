package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@Validated
public class MusicController {

    private final MusicService musicService;

    @Operation(summary = "Search for music based on music or author's name ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a set of music",
                    content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = MusicDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid number of characteres for music/author's name", content = @Content),
            @ApiResponse(responseCode = "204", description = "No music results found", content = @Content)
    })
    @GetMapping("/musicas")
    public ResponseEntity<Slice<Music>> findAllWithFilter(
            @RequestParam(name = "filtro", required = false)
            @Size(min = 2, message = "The filter must have at least 2 characters") final String filter,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size){

        return
                ResponseEntity.ok(musicService.findAllWithFilter(filter, page, size));
    }
}
