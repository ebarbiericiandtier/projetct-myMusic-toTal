package com.ciandt.summit.bootcamp2022.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Music", description = "Representation of a Playlist")
public class PlaylistDTO {

    @Schema(description = "Unique value that represents a Playlist in database")
    @NotNull
    private String id;
    @Schema(description = "Set of songs")
    private Set<MusicDTO> musics;
}
