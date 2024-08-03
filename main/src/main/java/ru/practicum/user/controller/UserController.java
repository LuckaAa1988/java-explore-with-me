package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventShortResponse;
import ru.practicum.dto.user.UserReactionResponse;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users/rating")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserReactionResponse>> findAllTop(@RequestParam(defaultValue = "0") Integer from,
                                                                 @RequestParam(defaultValue = "10") Integer size) throws InvalidParametersException {
        return ResponseEntity.ok(userService.findAllTopUsers(from, size));
    }
}
