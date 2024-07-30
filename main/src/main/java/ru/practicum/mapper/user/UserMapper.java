package ru.practicum.mapper.user;

import org.springframework.stereotype.Component;
import ru.practicum.dto.user.UserRequest;
import ru.practicum.dto.user.UserResponse;
import ru.practicum.dto.user.UserShortResponse;
import ru.practicum.user.entity.User;

@Component
public class UserMapper {

    public User fromDto(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
    }

    public UserResponse toDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortResponse toShortDto(User initiator) {
        return UserShortResponse.builder()
                .id(initiator.getId())
                .name(initiator.getName())
                .build();
    }
}
