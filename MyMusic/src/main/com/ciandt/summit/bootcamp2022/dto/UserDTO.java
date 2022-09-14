package com.ciandt.summit.bootcamp2022.dto;

import com.ciandt.summit.bootcamp2022.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(name = "User", description = "Model of a User")
public class UserDTO {

    @Schema(name = "id", description = "Unique user identifier")
    private UUID id;

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