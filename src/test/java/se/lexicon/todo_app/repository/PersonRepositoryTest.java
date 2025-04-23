package se.lexicon.todo_app.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.todo_app.entity.Person;

import java.util.Optional;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository testObject;

    // test save and find by email
    @Test
    @DisplayName("Save and find By Email")
    void testSaveAndFindByEmail() {
        Person person = Person.builder().name("Dev1").email("dev1@test.se").build();
        testObject.save(person);

        Optional<Person> found = testObject.findByEmail(person.getEmail());
        assertTrue(found.isPresent());
    }

    // test exists by email
    @Test
    @DisplayName("Test exists By Email")
    void testExistsByEmail() {
        // Arrange
        Person person = Person.builder().name("Dev2").email("dev2@test.se").build();
        testObject.save(person);

        // Act
        boolean exists = testObject.existsByEmail(person.getEmail());

        // Assert
        assertTrue(exists);
    }
}
