package io.github.jotabrc.ov_fma_user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.ov_fma_user.dto.UserCreationUpdateDto;
import io.github.jotabrc.ov_fma_user.dto.UserDto;

import java.security.NoSuchAlgorithmException;

public sealed interface UserService permits UserServiceImpl {

    UserDto save(UserCreationUpdateDto dto) throws NoSuchAlgorithmException, JsonProcessingException;
    UserDto update(String userUuid, UserCreationUpdateDto dto) throws NoSuchAlgorithmException, JsonProcessingException;
    UserDto get(String userUuid);
}
