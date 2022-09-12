package com.ciandt.summit.bootcamp2022.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "User", description = "Representation of a User")
public class UserDTO {

    @Schema(description = "Unique value that represents a User in database")
    @NotNull
    private String id;
    @Schema(description = "User's name")
    private String username;

}
