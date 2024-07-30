package ru.practicum.user.service;

import ru.practicum.dto.user.UserRequest;
import ru.practicum.dto.user.UserResponse;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers(Integer from, Integer size, Long[] ids) throws InvalidParametersException;

    UserResponse addUser(UserRequest userRequest) throws ConflictException;

    void removeUser(Long userId) throws NotFoundException;
}
