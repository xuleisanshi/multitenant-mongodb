package com.example.mongo.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.mongo.models.Person;
import com.example.mongo.models.PersonRepository;

@RestController
@RequestMapping(path = PersonController.PATH)
@RequestScope
@Validated
public class PersonController {
	public static final String PATH = "/api/v1.0/person";
	private PersonRepository personRepository;
	private MongoTemplate mongoTemplate;

	@Autowired
	HttpServletRequest request;

	@Inject
	public PersonController(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public Iterable<Person> persons() {
		return personRepository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Person personById(@PathVariable("id") String id) {
		return personRepository.findOne(id);
	}

	@RequestMapping(value = "/lastname/{name}", method = RequestMethod.GET)
	public Iterable<Person> PersonByName(@PathVariable("name") String name) {
		return personRepository.findByLastName(name);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Person> add(@RequestBody @Valid Person Person, UriComponentsBuilder uriComponentsBuilder) {
		Person savedPerson = personRepository.save(Person);
		UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{{id}").buildAndExpand(savedPerson.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<>(savedPerson, headers, HttpStatus.CREATED);

	}

	@DeleteMapping
	@ResponseStatus(NO_CONTENT)
	public void deteAll() {
		personRepository.deleteAll();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(NO_CONTENT)
	public void deleteById(@PathVariable("id") String id) {
		personRepository.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Person update(@PathVariable("id") String id, @RequestBody Person updatePerson) {
		return personRepository.save(updatePerson);
	}
}
