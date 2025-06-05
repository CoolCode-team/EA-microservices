package com.ea.accountservice.http.model;

import com.ea.accountservice.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record UserResponse(

        @Schema
        String id,


        @Schema
        String email,

        @Schema
        String name,

        @Schema
        UserRole role) {
}
