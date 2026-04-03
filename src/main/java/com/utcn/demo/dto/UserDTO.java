package com.utcn.demo.dto;

import com.utcn.demo.entity.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record UserDTO(Long id, String username, String email, BigDecimal score, List<String> roles) {
    public static UserDTO fromEntity(User user) {
        if (user == null)
            return null;
        List<String> roleNames = user.getRoles() != null
                ? user.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList())
                : List.of();
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getScore(), roleNames);
    }
}
