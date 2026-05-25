package com.utcn.demo.dto.Responses;

public record AuthResponseDTO(
        String token,
        UserResponseDTO user
) {
}