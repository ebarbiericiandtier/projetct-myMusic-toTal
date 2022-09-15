package com.ciandt.summit.bootcamp2022.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(name = "Artist", description = "Representation of a Artist or music's author")
@Builder
public class ArtistDTO {
    @Schema(description = "Unique value that represents a Artist in database")
    private String id;
    @Schema(description = "Artist's name")
    private String name;
}
