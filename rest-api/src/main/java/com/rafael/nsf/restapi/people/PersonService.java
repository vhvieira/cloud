package com.rafael.nsf.restapi.people;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    /**
     * Finds a person into database
     * @param id
     * @return
     * @throws InstanceNotFoundException
     */
    public Person findPerson(Long id) throws InstanceNotFoundException {
        Optional<Person> currentPerson = repository.findById(id);
        if(currentPerson.isPresent()) {
            return currentPerson.get();
        } else {
            throw new InstanceNotFoundException("Person not found");
        }
    }

    /**
     * Removes a person from database
     * @param id
     * @return
     * @throws InstanceNotFoundException
     */
    public void deletePerson(Long id) throws InstanceNotFoundException {
        Optional<Person> currentPerson = repository.findById(id);
        if(currentPerson.isPresent()) {
            repository.delete(currentPerson.get());
        } else {
            throw new InstanceNotFoundException("Person not found");
        }
    }


    /**
     * Save new person
     * @param person
     * @return
     */
    public Person savePerson(Person person) {
        if(person.getId() != null) {
            throw new IllegalArgumentException("Id shouldn't be provided for creating object");
        }
        return repository.save(person);
    }

    /**
     * Update an existing person
     * @param person
     * @return
     */
    public Person updatePerson(Person person) throws InstanceNotFoundException {
        if(person.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }
        Optional<Person> currentPerson = repository.findById(person.getId());
        if(currentPerson.isPresent()) {
            return repository.save(person);
        } else {
            throw new InstanceNotFoundException("Could not update instance");
        }
    }

    /**
     * Return all persons
     * @return
     */
    public List<Person> listAllPeople() {
        return (List<Person>) repository.findAll();
    }
}
