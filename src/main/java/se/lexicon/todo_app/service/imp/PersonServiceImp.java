package se.lexicon.todo_app.service.imp;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.lexicon.todo_app.dto.PersonDto;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.exception.DataDuplicateException;
import se.lexicon.todo_app.exception.MyExceptionHandler;
import se.lexicon.todo_app.exception.ResourceNotFoundException;
import se.lexicon.todo_app.repository.PersonRepository;
import se.lexicon.todo_app.service.PersonService;

import java.util.List;

@Service
public class PersonServiceImp implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImp(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<PersonDto> findAll() {
        List<Person> personList = personRepository.findAll();
        if(personList.isEmpty()) {
            throw new ResourceNotFoundException("No persons found");
        }
        return personList.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public PersonDto findById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));
        return convertToDto(person);
    }

    @Override
    public PersonDto createPerson(PersonDto personDto) {
        if(personRepository.existsByEmail(personDto.email())) {
            throw new DataDuplicateException("Email " + personDto.email() + " already exists");
        }

        Person person = new Person(personDto.name(), personDto.email());
        Person createdPerson = personRepository.save(person);
        return convertToDto(createdPerson);
    }

    @Override
    public void updatePerson(Long id, PersonDto personDto) {
        Person original = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));

        if(!original.getEmail().equals(personDto.email()) &&
                personRepository.existsByEmail(personDto.email())) {
            throw new DataDuplicateException("Email " + personDto.email() + " already exists");
        }

        original.setName(personDto.name());
        original.setEmail(personDto.email());
        personRepository.save(original);
    }

    @Override
    public void deletePerson(Long id) {
        if(!personRepository.existsById(id)) {
            throw new ResourceNotFoundException("Person with id " + id + " not found");
        }
        personRepository.deleteById(id);
    }

    private PersonDto convertToDto(Person person) {
        return new PersonDto(person.getId(), person.getName(), person.getEmail());
    }
}
