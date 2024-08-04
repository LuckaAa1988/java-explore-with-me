package ru.practicum.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserReactionResponse;
import ru.practicum.dto.user.UserRequest;
import ru.practicum.dto.user.UserResponse;
import ru.practicum.event.repository.ReactionRepository;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.util.Constants;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.user.entity.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.service.UserService;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> findAll(Integer from, Integer size, Long[] ids) throws InvalidParametersException {
        if (from < 0) throw new InvalidParametersException("Invalid parameters");
        var pageable = PageRequest.of(from / size, size);
        log.info("Get all users by ids: {} with pageable {}", ids, pageable);
        List<User> response = ids == null ? userRepository.findAll(pageable).getContent() :
                userRepository.findAllByIdIn(Arrays.asList(ids), pageable).getContent();
        return response.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse save(UserRequest userRequest) throws ConflictException {
        log.info("Add user with request: {}", userRequest);
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ConflictException("Duplicate user email.");
        }
        return userMapper.toDto(userRepository.save(userMapper.fromDto(userRequest)));
    }

    @Override
    @Transactional
    public void deleteById(Long userId) throws NotFoundException {
        log.info("Remove user with id: {}", userId);
        if (userRepository.deleteUserById(userId) == 0) {
            throw new NotFoundException(String.format(Constants.USER_NOT_FOUND, userId));
        }
    }

    @Override
    public List<UserReactionResponse> findAllTopUsers(Integer from, Integer size) throws InvalidParametersException {
        if (from < 0) throw new InvalidParametersException("Invalid parameters");
        var pageable = PageRequest.of(from / size, size);
        log.info("Get all top events with pageable: {}", pageable);
        return reactionRepository.findAllTopUsers(from / size, size).stream()
                .map(tuple -> {
                    int reaction = 0;
                    if (tuple.get(2, BigInteger.class) != null) {
                        reaction = tuple.get(2, BigInteger.class).intValue();
                    }
                    return UserReactionResponse.builder()
                            .id(tuple.get(0, BigInteger.class).longValue())
                            .name(tuple.get(1, String.class))
                            .reaction(reaction)
                            .build();
                })
                .sorted(Comparator.comparing(UserReactionResponse::getReaction).reversed())
                .collect(Collectors.toList());
    }
}
