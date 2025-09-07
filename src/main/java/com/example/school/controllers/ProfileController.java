package com.example.school.controllers;

import com.example.school.model.Address;
import com.example.school.model.Person;
import com.example.school.model.Profile;
import com.example.school.repository.PersonRepository;
import com.example.school.service.PersonService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller("profileControllerBean")
public class ProfileController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/displayProfile")
    public ModelAndView displayProfilePage(Model model, HttpSession session) {
        Person loggedInPerson = (Person) session.getAttribute("loggedInPerson");

        Profile profile = new Profile();

        profile.setName(loggedInPerson.getName());
        profile.setEmail(loggedInPerson.getEmail());
        profile.setMobileNumber(loggedInPerson.getMobileNumber());
        if(null!=loggedInPerson.getAddress() && loggedInPerson.getAddress().getAddressId()>0){
            profile.setAddress1(loggedInPerson.getAddress().getAddress1());
            profile.setAddress2(loggedInPerson.getAddress().getAddress2());
            profile.setCity(loggedInPerson.getAddress().getCity());
            profile.setState(loggedInPerson.getAddress().getState());
            profile.setZipCode(loggedInPerson.getAddress().getZipCode());
        }
        ModelAndView modelAndView = new ModelAndView("profile.html");
        modelAndView.addObject("profile", profile);
        return modelAndView;
    }

    @PostMapping("/updateProfile")
    public String updateProfilePage(@Valid @ModelAttribute("profile") Profile profile, Errors errors, HttpSession session){
        if(errors.hasErrors()){
            return "profile.html";
        }
        Person loggedInPerson = (Person) session.getAttribute("loggedInPerson");
        loggedInPerson.setName(profile.getName());
        loggedInPerson.setEmail(profile.getEmail());
        loggedInPerson.setMobileNumber(profile.getMobileNumber());
        if(null==loggedInPerson.getAddress() || !(loggedInPerson.getAddress().getAddressId()>0)){
            loggedInPerson.setAddress(new Address());
        }
        loggedInPerson.getAddress().setAddress1(profile.getAddress1());
        loggedInPerson.getAddress().setAddress2(profile.getAddress2());
        loggedInPerson.getAddress().setCity(profile.getCity());
        loggedInPerson.getAddress().setState(profile.getState());
        loggedInPerson.getAddress().setZipCode(profile.getZipCode());
        Person savedPerson = personRepository.save(loggedInPerson);
        session.setAttribute("loggedInPerson", savedPerson);
        return "redirect:/displayProfile";
    }
}
