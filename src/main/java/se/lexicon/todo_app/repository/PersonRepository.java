package se.lexicon.todo_app.repository;

import org.springframework.data.repository.CrudRepository;
import se.lexicon.todo_app.entity.Person;

import java.util.Optional;


public interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    boolean existsByEmail(String email);
}
