package se.lexicon.todo_app.service.imp;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.todo_app.dto.AttachmentDto;
import se.lexicon.todo_app.dto.TodoDto;
import se.lexicon.todo_app.entity.*;
import se.lexicon.todo_app.exception.*;
import se.lexicon.todo_app.repository.*;
import se.lexicon.todo_app.service.TodoService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final PersonRepository personRepository;
    private final AttachmentRepository attachmentRepository;

    public TodoServiceImpl(TodoRepository todoRepository,
                           PersonRepository personRepository,
                           AttachmentRepository attachmentRepository) {
        this.todoRepository = todoRepository;
        this.personRepository = personRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public TodoDto findById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        return toDto(todo);
    }

    @Override
    public List<TodoDto> findAll() {
        return todoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByPersonId(Long personId) {
        return todoRepository.findByPersonId(personId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByTitle(String title) {
        return todoRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TodoDto create(TodoDto todoDto) {
        Todo todo = new Todo();
        todo.setTitle(todoDto.title());
        todo.setDescription(todoDto.description());
        todo.setCompleted(todoDto.completed());
        todo.setDueDate(todoDto.dueDate());

        if (todoDto.personId() != null) {
            Person person = personRepository.findById(todoDto.personId())
                    .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + todoDto.personId()));
            todo.setPerson(person);
        }

        Todo savedTodo = todoRepository.save(todo);
        return toDto(savedTodo);
    }

    @Override
    @Transactional
    public TodoDto update(Long id, TodoDto todoDto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

        todo.setTitle(todoDto.title());
        todo.setDescription(todoDto.description());
        todo.setCompleted(todoDto.completed());
        todo.setDueDate(todoDto.dueDate());

        if (todoDto.personId() != null) {
            Person person = personRepository.findById(todoDto.personId())
                    .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + todoDto.personId()));
            todo.setPerson(person);
        } else {
            todo.setPerson(null);
        }

        Todo updatedTodo = todoRepository.save(todo);
        return toDto(updatedTodo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TodoDto addAttachment(Long todoId, AttachmentDto attachmentDto) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + todoId));

        Attachment attachment = new Attachment();
        attachment.setFileName(attachmentDto.fileName());
        attachment.setFileType(attachmentDto.fileType());
        attachment.setTodo(todo);

        attachmentRepository.save(attachment);
        return toDto(todo);
    }

    @Override
    @Transactional
    public void removeAttachment(Long todoId, Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id: " + attachmentId));

        if (!attachment.getTodo().getId().equals(todoId)) {
            throw new ValidationException("Attachment does not belong to the specified todo");
        }

        attachmentRepository.delete(attachment);
    }

    private TodoDto toDto(Todo todo) {
        Set<AttachmentDto> attachmentDtos = todo.getAttachments().stream()
                .map(attachment -> new AttachmentDto(
                        attachment.getId(),
                        attachment.getFileName(),
                        attachment.getFileType(),
                        todo.getId()))
                .collect(Collectors.toSet());

        Long personId = todo.getPerson() != null ? todo.getPerson().getId() : null;

        return new TodoDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted(),
                todo.getCreatedAt(),
                todo.getUpdatedAt(),
                todo.getDueDate(),
                personId,
                attachmentDtos);
    }
}
