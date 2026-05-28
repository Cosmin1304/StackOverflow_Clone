package com.utcn.demo.dto.Mappers;

import com.utcn.demo.dto.Requests.LoginRequestDTO;
import com.utcn.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public User toEntity(LoginRequestDTO loginDTO) {
        if (loginDTO == null) {
            return null;
        }

        User user = new User();
        user.setUsername(loginDTO.username());
        user.setPasswordHash(loginDTO.password());

        return user;
    }
}
