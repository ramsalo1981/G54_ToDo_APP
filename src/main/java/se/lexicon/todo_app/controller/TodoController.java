package se.lexicon.todo_app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.todo_app.dto.AttachmentDto;
import se.lexicon.todo_app.dto.TodoDto;
import se.lexicon.todo_app.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<List<TodoDto>> findAll() {
        return ResponseEntity.ok(todoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findById(id));
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<TodoDto>> findByPersonId(@PathVariable Long personId) {
        return ResponseEntity.ok(todoService.findByPersonId(personId));
    }

    @GetMapping("/completed/{completed}")
    public ResponseEntity<List<TodoDto>> findByCompleted(@PathVariable boolean completed) {
        return ResponseEntity.ok(todoService.findByCompleted(completed));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TodoDto>> findByTitle(@RequestParam String title) {
        return ResponseEntity.ok(todoService.findByTitle(title));
    }

    @PostMapping
    public ResponseEntity<TodoDto> create(@RequestBody @Valid TodoDto todoDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(todoService.create(todoDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> update(@PathVariable Long id, @RequestBody @Valid TodoDto todoDto) {
        return ResponseEntity.ok(todoService.update(id, todoDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        todoService.delete(id);
    }

    @PostMapping("/{todoId}/attachments")
    public ResponseEntity<TodoDto> addAttachment(
            @PathVariable Long todoId,
            @RequestBody @Valid AttachmentDto attachmentDto) {
        return ResponseEntity.ok(todoService.addAttachment(todoId, attachmentDto));
    }

    @DeleteMapping("/{todoId}/attachments/{attachmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAttachment(
            @PathVariable Long todoId,
            @PathVariable Long attachmentId) {
        todoService.removeAttachment(todoId, attachmentId);
    }
}
