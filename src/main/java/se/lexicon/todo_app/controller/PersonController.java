package se.lexicon.todo_app.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.todo_app.dto.PersonDto;
import se.lexicon.todo_app.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@Validated
public class PersonController {

    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)//200
    public List<PersonDto> findAll() {
        return personService.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)//200
    public PersonDto findById(@PathVariable("id")
                                  @NotNull(message = "Id must be not null")
                                  @Positive(message = "Id must be positive")
                                  Long id) {
        System.out.println("findById: " + id);
        return personService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)//201
    public PersonDto create(@RequestBody @Valid PersonDto personDto) {
        System.out.println("create: " + personDto);
        return personService.createPerson(personDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    public void updatePerson(@PathVariable("id")
                             @NotNull(message = "Id cannot be null")
                             @Positive(message = "Id must be a positive number")
                             Long id,
                             @RequestBody
                             @Valid
                             PersonDto personDto) {

        personService.updatePerson(id, personDto);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    public void deletePerson(@PathVariable("id")
                             @NotNull(message = "Id cannot be null")
                             @Positive(message = "Id must be a positive number")
                             Long id) {
        personService.deletePerson(id);
    }
}
