package se.lexicon.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import se.lexicon.todo_app.entity.Todo;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // Custom query methods can be defined here if needed
    List<Todo> findByPersonId(Long personId);
    List<Todo> findByCompleted(boolean completed);
    List<Todo> findByTitleContainingIgnoreCase(String title);
}
