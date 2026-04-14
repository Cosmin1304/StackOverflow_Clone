package com.utcn.demo.dto.Responses;

import java.math.BigDecimal;
import java.util.List;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        BigDecimal score,
        List<String> roles
) {}
