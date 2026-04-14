package com.utcn.demo.dto.Requests;

public record UserRequestDTO(
        String userName,
        String userEmail,
        String userPassword
) {
}
