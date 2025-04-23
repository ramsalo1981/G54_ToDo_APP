package se.lexicon.todo_app.repository;

import org.springframework.data.repository.CrudRepository;
import se.lexicon.todo_app.entity.Todo;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    // Custom query methods can be defined here if needed
}
