package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.pharmacywebsite.domain.Person;

@RepositoryRestResource
public interface PersonRepo extends JpaRepository<Person, Long> {

}
