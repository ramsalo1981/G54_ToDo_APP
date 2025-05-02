package se.lexicon.todo_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PersonDto(
        Long id,
        @NotBlank
        @Size(min = 2, max = 50, message = "name must be between 2 to 50")
        String name,
        @NotBlank
        @Size(min = 5, max = 150, message = "name must be between 5 to 150")
        @Email(message = "Invalid email Format")
        String email) {
}
