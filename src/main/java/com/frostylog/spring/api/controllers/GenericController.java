package com.frostylog.spring.api.controllers;

import com.frostylog.spring.lib.models.Person;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenericController {
    @RequestMapping(value = "/api/v1/", method = RequestMethod.POST)
    public Person getPersonByName(@RequestBody final Person pPerson) {
        // Will echo object that is passed to it.
        return pPerson;
    }

}