package se.lexicon.todo_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

public record TodoDto(
        Long id,

        @NotBlank(message = "Title is required")
        @Size(min = 2, max = 100, message = "Title must be between 2-100 characters")
        String title,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        boolean completed,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime dueDate,

        Long personId,

        Set<AttachmentDto> attachments
) {
    public TodoDto {
        attachments = attachments != null ? attachments : Set.of();
    }
}
