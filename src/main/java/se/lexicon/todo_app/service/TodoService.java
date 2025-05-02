package se.lexicon.todo_app.service;

import se.lexicon.todo_app.dto.AttachmentDto;
import se.lexicon.todo_app.dto.TodoDto;

import java.util.List;

public interface TodoService {
    TodoDto findById(Long id);
    List<TodoDto> findAll();
    List<TodoDto> findByPersonId(Long personId);
    List<TodoDto> findByCompleted(boolean completed);
    List<TodoDto> findByTitle(String title);
    TodoDto create(TodoDto todoDto);
    TodoDto update(Long id, TodoDto todoDto);
    void delete(Long id);
    TodoDto addAttachment(Long todoId, AttachmentDto attachmentDto);
    void removeAttachment(Long todoId, Long attachmentId);
}
