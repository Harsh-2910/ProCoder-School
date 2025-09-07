package com.example.school.service;

import com.example.school.constants.ProCoderConstants;
import com.example.school.model.Person;
import com.example.school.model.Roles;
import com.example.school.repository.PersonRepository;
import com.example.school.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public boolean createNewPerson(Person person){
        boolean isSaved = false;
        Roles role = rolesRepository.getByRoleName(ProCoderConstants.STUDENT_ROLE);
        person.setRole(role);
        person.setPwd(passwordEncoder.encode(person.getPwd()));
        person = personRepository.save(person);
        if(null!=person && person.getPersonId()>0){
            isSaved = true;
        }
        return isSaved;
    }
}
