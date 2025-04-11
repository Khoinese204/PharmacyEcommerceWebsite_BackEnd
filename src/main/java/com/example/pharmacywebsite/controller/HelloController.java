package com.example.pharmacywebsite.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pharmacywebsite.domain.Person;
import com.example.pharmacywebsite.repository.PersonRepo;

@RestController
public class HelloController {

    private final PersonRepo repo;

    public HelloController(PersonRepo repo) {
        this.repo = repo;
    }

    @PostMapping("/addPerson")
    public Person addPerson(@RequestBody Person p) {
        return repo.save(p);
    }
}
