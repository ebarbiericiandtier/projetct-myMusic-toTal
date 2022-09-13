package com.ciandt.summit.bootcamp2022.dto;

import com.ciandt.summit.bootcamp2022.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "User", description = "Model of a User")
public class UserDto {

    @Schema(name = "id", description = "Unique user identifier")
    private String id;

    @Schema(name = "username", description = "user's name", required = true)
    @NotBlank
    @NotNull
    private String username;

    @Schema(name = "role", description = "role of user (premium/comum)", required = true)
    @NotNull
    private Role role;

    @Schema(name = "playlist", description = "User's playlist")
    private PlaylistDTO playlist;
}
