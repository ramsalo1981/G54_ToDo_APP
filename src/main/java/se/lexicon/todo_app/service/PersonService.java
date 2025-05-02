package se.lexicon.todo_app.service;

import se.lexicon.todo_app.dto.PersonDto;

import java.util.List;

public interface PersonService {
    List<PersonDto> findAll();
    PersonDto findById(Long id);
    PersonDto createPerson(PersonDto personDto);
    void updatePerson(Long id,PersonDto personDto);
    void deletePerson(Long id);
}
