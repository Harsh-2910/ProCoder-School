package com.example.school.controllers;

import com.example.school.model.Courses;
import com.example.school.model.Person;
import com.example.school.model.ProCoderClass;
import com.example.school.repository.CoursesRepository;
import com.example.school.repository.PersonRepository;
import com.example.school.repository.ProCoderClassRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    ProCoderClassRepository proCoderClassRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses() {
        List<ProCoderClass> proClasses = proCoderClassRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("classes.html");
        modelAndView.addObject("proClasses", proClasses);
        modelAndView.addObject("proClass", new ProCoderClass());
        return modelAndView;
    }

    @PostMapping("/addNewClass")
    public ModelAndView addNewClass(Model model, @ModelAttribute("proClass") ProCoderClass proCoderClass){
        proCoderClassRepository.save(proCoderClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass(Model model, @RequestParam int id){
        Optional<ProCoderClass> proClasses = proCoderClassRepository.findById(id);
        for(Person person : proClasses.get().getPersons()){
            person.setProCoderClass(null);
            personRepository.save(person);
        }
        proCoderClassRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping("/displayStudents")
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession httpSession, @RequestParam(value = "error", required = false) String error){
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<ProCoderClass> proClass = proCoderClassRepository.findById(classId);
        modelAndView.addObject("proClass", proClass.get());
        modelAndView.addObject("person", new Person());
        httpSession.setAttribute("proClass", proClass.get());
        if(error != null){
            errorMessage = "Invalid Student Email ID Entered!!";
            modelAndView.addObject("errorMessage",errorMessage);
        }
        return modelAndView;
    }

    @RequestMapping("/addStudent")
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView();
        ProCoderClass proClass = (ProCoderClass) httpSession.getAttribute("proClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(null==personEntity || !(personEntity.getPersonId()>0)){
            modelAndView.setViewName("redirect:/admin/displayStudents?classId="+proClass.getClassId()+"&error=true");
            return modelAndView;
        }
        personEntity.setProCoderClass(proClass);
        personRepository.save(personEntity);
        proClass.getPersons().add(personEntity);
        proCoderClassRepository.save(proClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId="+proClass.getClassId());
        return modelAndView;
    }

    @RequestMapping("/deleteStudent")
    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession httpSession){
        ProCoderClass proClass = (ProCoderClass) httpSession.getAttribute("proClass");
        Optional<Person> personEntity = personRepository.findById(personId);
        personEntity.get().setProCoderClass(null);
        personRepository.save(personEntity.get());
        proClass.getPersons().remove(personEntity.get());
        ProCoderClass savedProClass = proCoderClassRepository.save(proClass);
        httpSession.setAttribute("proClass", savedProClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId="+savedProClass.getClassId());
        return modelAndView;
    }

    @GetMapping("/displayCourses")
    public ModelAndView displayCourses(Model model){
//        List<Courses> courses = coursesRepository.findByOrderByName();
        List<Courses> courses = coursesRepository.findAll(Sort.by("name").ascending());
        ModelAndView modelAndView = new ModelAndView("courses_secure.html");
        modelAndView.addObject("courses", courses);
        modelAndView.addObject("course", new Courses());
        return modelAndView;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Courses courses){
        coursesRepository.save(courses);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayCourses");
        return modelAndView;
    }

    @GetMapping("/viewStudents")
    public ModelAndView viewStudents(Model model, @RequestParam int id, HttpSession httpSession, @RequestParam(required = false) String error){
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("courses_students.html");
        Optional<Courses> courses = coursesRepository.findById(id);
        modelAndView.addObject("courses", courses.get());
        modelAndView.addObject("person", new Person());
        httpSession.setAttribute("courses", courses.get());
        if(error != null){
            errorMessage = "Invalid Student Email ID Entered!!";
            modelAndView.addObject("errorMessage",errorMessage);
        }
        return modelAndView;
    }

    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(Model model, @ModelAttribute("person") Person person, HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView();
        Person personEntity = personRepository.readByEmail(person.getEmail());
        Courses courses = (Courses) httpSession.getAttribute("courses");
        if(null==personEntity || !(personEntity.getPersonId()>0)){
            modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId()+"&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(courses);
        courses.getPersons().add(personEntity);
        personRepository.save(personEntity);
        httpSession.setAttribute("courses", courses);
        modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }

    @GetMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId, HttpSession httpSession){
        Courses courses = (Courses) httpSession.getAttribute("courses");
        Optional<Person> personEntity = personRepository.findById(personId);
        personEntity.get().getCourses().remove(courses);
        courses.getPersons().remove(personEntity.get());
        personRepository.save(personEntity.get());
        httpSession.setAttribute("courses", courses);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }
}
