package com.rafael.nsf.restapi.people;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.util.List;

@RequestMapping("/person")
@RestController
@Slf4j
public class PersonController {

    @Autowired
    private PersonService service;

    @Autowired
    private UploadService uploadService;

    @GetMapping("/upload")
    public ResponseEntity<Person> upload() {
        try {
            uploadService.sendSimpleFileToJira();
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Exception", ex);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/uploads")
    public ResponseEntity<Person> uploads() {
        try {
            uploadService.sendUploadFiles();
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Exception", ex);
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable("personId") Long personId) {
        try {
            return ResponseEntity.ok(service.findPerson(personId));
        } catch (InstanceNotFoundException ex) {
            log.error("Person not found", ex);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity deletePerson(@PathVariable("personId") Long personId) {
        try {
            service.deletePerson(personId);
            return ResponseEntity.ok().build();
        } catch (InstanceNotFoundException ex) {
            log.error("Person not found", ex);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        log.info("Recebeu objeto para atualizar: " + person);
        try {
            return ResponseEntity.ok(service.updatePerson(person));
        } catch (IllegalArgumentException exception) {
            log.error("Parametro inválido", exception);
            return ResponseEntity.badRequest().build();
        } catch (InstanceNotFoundException exception) {
            log.error("ID inválido", exception);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        log.info("Recebeu objeto para salvar: " + person);
        try {
            return ResponseEntity.ok(service.savePerson(person));
        } catch (IllegalArgumentException exception) {
            log.error("Parametro inválido", exception);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> listPerson() {
        List<Person> list = service.listAllPeople();
        if(CollectionUtils.isEmpty(list)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(list);
        }
    }

}
