package ru.practicum.admin.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.dto.category.CategoryRequest;
import ru.practicum.dto.category.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.category.service.CategoryService;
import ru.practicum.dto.compilation.CompilationRequest;
import ru.practicum.dto.compilation.CompilationResponse;
import ru.practicum.dto.compilation.CompilationUpdateRequest;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.dto.event.EventResponse;
import ru.practicum.dto.user.UserRequest;
import ru.practicum.dto.user.UserResponse;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final CompilationService compilationService;
    private final EventService eventService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody @Valid CategoryRequest categoryRequest) throws ConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(categoryRequest));
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Object> removeCategory(@PathVariable Long categoryId) throws ConflictException, NotFoundException {
        categoryService.removeCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody @Valid CategoryRequest categoryRequest,
                                                           @PathVariable Long categoryId) throws NotFoundException, ConflictException {
        return ResponseEntity.ok(categoryService.updateCategory(categoryRequest, categoryId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") Integer from,
                                                          @RequestParam(defaultValue = "10") Integer size,
                                                          @RequestParam(required = false) Long[] ids) throws InvalidParametersException {
        return ResponseEntity.ok(userService.getAllUsers(from, size, ids));
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> addUser(@RequestBody @Valid UserRequest userRequest) throws ConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequest));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> removeUser(@PathVariable Long userId) throws NotFoundException {
        userService.removeUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/compilations")
    public ResponseEntity<CompilationResponse> addCompilation(@RequestBody @Valid CompilationRequest compilationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.addCompilation(compilationRequest));
    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<Object> removeCompilation(@PathVariable Long compId) throws NotFoundException {
        compilationService.removeCompilation(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<CompilationResponse> updateCompilation(@PathVariable Long compId,
                                                                 @RequestBody @Valid CompilationUpdateRequest compilationRequest) throws NotFoundException {
        return ResponseEntity.ok(compilationService.updateCompilation(compId, compilationRequest));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getAllEvents(@RequestParam(required = false) Integer[] users,
                                                            @RequestParam(required = false) String[] states,
                                                            @RequestParam(required = false) Integer[] categories,
                                                            @RequestParam(required = false) String rangeStart,
                                                            @RequestParam(required = false) String rangeEnd,
                                                            @RequestParam(defaultValue = "0") Integer from,
                                                            @RequestParam(defaultValue = "10") Integer size) throws InvalidParametersException {
        return ResponseEntity.ok(eventService.getAllEvents(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventResponse> updateAdminEvent(@PathVariable Long eventId,
                                                          @RequestBody @Valid EventUpdateRequest eventUpdateRequest) throws NotFoundException, ConflictException {
        return ResponseEntity.ok(eventService.updateAdminEvent(eventId, eventUpdateRequest));
    }
}
