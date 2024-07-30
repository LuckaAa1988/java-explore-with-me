package ru.practicum.dto.compilation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationRequest {
    List<Long> events;
    Boolean pinned = false;
    @NotNull
    @NotBlank
    @Size(max = 50)
    String title;
}
