package com.example.school.controllers;

import com.example.school.model.Person;
import com.example.school.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class DashboardController {
    @Autowired
    private PersonRepository personRepository;

    @Value("${procoder.pageSize}")
    private int defaultPageSize;

    @Value("${procoder.contact.successMsg}")
    private String message;

    @Autowired
    Environment environment;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession session) {
        Person person = personRepository.readByEmail(authentication.getName());
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        if(null!=person.getProCoderClass() && null!=person.getProCoderClass().getName()){
            model.addAttribute("enrolledClass",person.getProCoderClass().getName());
        }
        session.setAttribute("loggedInPerson", person);
//        throw new RuntimeException("Page not found");
        logMessages();
        return "dashboard.html";
    }

    private void logMessages() {
        log.error("Error message from the Dashboard page");
        log.warn("Warning message from the Dashboard page");
        log.info("Info message from the Dashboard page");
        log.debug("Debug message from the Dashboard page");
        log.trace("Trace message from the Dashboard page");

        log.error("default page size value with @Value annotation" + defaultPageSize);
        log.error("success message value with @Value annotation" + message);

        log.error("defaultPageSize value with Environment is : "+environment.getProperty("procoder.pageSize"));
        log.error("successMsg value with Environment is : "+environment.getProperty("procoder.contact.successMsg"));
        log.error("Java Home environment variable using Environment is : "+environment.getProperty("JAVA_HOME"));
    }
}
