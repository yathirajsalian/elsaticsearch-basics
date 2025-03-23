package com.ygn.elasticsearch.Controller;

import com.ygn.elasticsearch.Document.Person;
import com.ygn.elasticsearch.Service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService service;

    @Autowired
    public PersonController(PersonService service)
    {
        this.service = service;
    }

    @PostMapping
    public void save(@RequestBody final Person person)
    {
        service.save(person);
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable final String id)
    {
        return service.findById(id);
    }

}
