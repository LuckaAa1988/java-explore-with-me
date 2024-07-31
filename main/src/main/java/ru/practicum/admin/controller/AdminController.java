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
    public ResponseEntity<CategoryResponse> saveCategory(@RequestBody @Valid CategoryRequest categoryRequest) throws ConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(categoryRequest));
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long categoryId) throws ConflictException, NotFoundException {
        categoryService.deleteById(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody @Valid CategoryRequest categoryRequest,
                                                           @PathVariable Long categoryId) throws NotFoundException, ConflictException {
        return ResponseEntity.ok(categoryService.update(categoryRequest, categoryId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> findAllUsers(@RequestParam(defaultValue = "0") Integer from,
                                                          @RequestParam(defaultValue = "10") Integer size,
                                                          @RequestParam(required = false) Long[] ids) throws InvalidParametersException {
        return ResponseEntity.ok(userService.findAll(from, size, ids));
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserRequest userRequest) throws ConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequest));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) throws NotFoundException {
        userService.deleteById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/compilations")
    public ResponseEntity<CompilationResponse> saveCompilation(@RequestBody @Valid CompilationRequest compilationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.save(compilationRequest));
    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compId) throws NotFoundException {
        compilationService.deleteById(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<CompilationResponse> updateCompilation(@PathVariable Long compId,
                                                                 @RequestBody @Valid CompilationUpdateRequest compilationRequest) throws NotFoundException {
        return ResponseEntity.ok(compilationService.update(compId, compilationRequest));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> findAllEvents(@RequestParam(required = false) Integer[] users,
                                                            @RequestParam(required = false) String[] states,
                                                            @RequestParam(required = false) Integer[] categories,
                                                            @RequestParam(required = false) String rangeStart,
                                                            @RequestParam(required = false) String rangeEnd,
                                                            @RequestParam(defaultValue = "0") Integer from,
                                                            @RequestParam(defaultValue = "10") Integer size) throws InvalidParametersException {
        return ResponseEntity.ok(eventService.findAll(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventResponse> updateAdminEvent(@PathVariable Long eventId,
                                                          @RequestBody @Valid EventUpdateRequest eventUpdateRequest) throws NotFoundException, ConflictException {
        return ResponseEntity.ok(eventService.update(eventId, eventUpdateRequest));
    }
}
