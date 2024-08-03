package ru.practicum.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReactionResponse {
    Long id;
    String name;
    Integer reaction;
}
