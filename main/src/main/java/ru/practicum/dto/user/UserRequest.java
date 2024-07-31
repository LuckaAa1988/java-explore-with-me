package ru.practicum.dto.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    String name;
    @Email
    @NotNull
    @NotBlank
    @Size(min = 6, max = 254)
    String email;
}
