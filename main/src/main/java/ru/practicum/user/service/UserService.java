package ru.practicum.user.service;

import ru.practicum.dto.event.EventShortResponse;
import ru.practicum.dto.user.UserReactionResponse;
import ru.practicum.dto.user.UserRequest;
import ru.practicum.dto.user.UserResponse;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll(Integer from, Integer size, Long[] ids) throws InvalidParametersException;

    UserResponse save(UserRequest userRequest) throws ConflictException;

    void deleteById(Long userId) throws NotFoundException;

    List<UserReactionResponse> findAllTopUsers(Integer from, Integer size) throws InvalidParametersException;
}
