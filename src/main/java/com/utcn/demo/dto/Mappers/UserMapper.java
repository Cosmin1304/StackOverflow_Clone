package com.utcn.demo.dto.Mappers;

import com.utcn.demo.dto.Requests.UserRequestDTO;
import com.utcn.demo.dto.Responses.UserResponseDTO;
import com.utcn.demo.entity.Role;
import com.utcn.demo.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponseDTO toResponse(User user){
        if(user == null) return null;
        List<String>roles = mapRolesToString(user.getRoles());
        return new UserResponseDTO(user.getId(),user.getUsername(), user.getEmail(), user.getScore(),roles);


    }
    private List<String> mapRolesToString(List<Role> roles){
        return Optional.ofNullable(roles)
                .orElse(List.of())
                .stream()
                .filter(Objects::nonNull)
                .map(Role::getRoleName)
                .toList();
    }
    public User toEntity(UserRequestDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.userName());
        user.setEmail(userDTO.userEmail());
        user.setScore(BigDecimal.ZERO);
        user.setIsBanned(false);
        user.setPasswordHash(userDTO.userPassword());
        return user;
    }


}
