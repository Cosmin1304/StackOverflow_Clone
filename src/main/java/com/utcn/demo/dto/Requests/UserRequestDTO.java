package com.utcn.demo.dto.Requests;

public record UserRequestDTO(
        String username,
        String email,
        String password,
        String phoneNumber
) {
}
