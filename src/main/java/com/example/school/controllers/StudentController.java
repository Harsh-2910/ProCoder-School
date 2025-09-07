package com.example.school.controllers;

import com.example.school.model.Person;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/student")
public class StudentController {
    @GetMapping("/displayCourses")
    public ModelAndView displayCoureses(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Person person = (Person)session.getAttribute("loggedInPerson");
        modelAndView.addObject("person",person);
        modelAndView.setViewName("courses_enrolled.html");
        return modelAndView;
    }
}
