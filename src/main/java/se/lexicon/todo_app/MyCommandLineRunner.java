package se.lexicon.todo_app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import se.lexicon.todo_app.entity.Attachment;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.entity.Todo;
import se.lexicon.todo_app.repository.PersonRepository;
import se.lexicon.todo_app.repository.TodoRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final TodoRepository todoRepository;

    public MyCommandLineRunner(PersonRepository personRepository, TodoRepository todoRepository) {
        this.personRepository = personRepository;
        this.todoRepository = todoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 👨‍💻 Create Developers
        Person dev1 = personRepository.save(new Person( "Dev1", "dev1@test.se"));
        Person dev2 = personRepository.save(new Person( "Dev2", "dev2@test.se"));
        Person dev3 = personRepository.save(new Person( "Dev3", "dev3@test.se"));


        // 🧠 Create Programming Tasks
        Todo t1 = new Todo("Implement Login", "Use Spring Security", false, LocalDateTime.now().plusDays(1));
        Todo t2 = new Todo("Create REST API", "Use Spring Web", false, LocalDateTime.now().plusDays(2));
        Todo t3 = new Todo("Database Schema", "Design tables", false, LocalDateTime.now().plusDays(3));
        Todo t4 = new Todo("Unit Tests", "Write JUnit tests", false, LocalDateTime.now().minusDays(1)); // overdue
        Todo t5 = new Todo("Frontend UI", "Build with React", false); // no due date

        Attachment file1 = new Attachment("unit-test-guide.pdf", "application/pdf", "Sample PDF Content".getBytes());
        file1.setTodo(t4); // safely sync both sides

        Attachment file2 = new Attachment("ui-mockup.png", "image/png", "Sample image content".getBytes());
        file2.setTodo(t5);


        Path path = Paths.get("img/todo_app.png"); // Adjust path as needed
        byte[] imageData = Files.readAllBytes(path);
        Attachment file3 = new Attachment("todo_app.png", "image/png", imageData);
        t4.addAttachment(file3); // add to task 4 from the inverse side


        // ✅ Assign Tasks
        t1.setPerson(dev1);
        t2.setPerson(dev1);
        t3.setPerson(dev1);
        t4.setPerson(dev3); // dev3 gets one task
        // t5 remains unassigned

        todoRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5));



    }
}
