package com.example.student.controller;
import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
@Controller
@RequestMapping("/students/")

public class StudentController {
    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("signup")
    public String showSignUpForm(Student student) {
        logger.info("Displaying sign-up form");
        return "add-student";
    }

    @GetMapping("list")
    public String showStudentList(Model model) {
        logger.info("Displaying student list");
        model.addAttribute("students", studentRepository.findAll());
        return "index";
    }

    @PostMapping("add")
    public String addStudent(@Valid Student student, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Error occurred while adding student: {}", result.getAllErrors());
            return "add-student";
        }

        // Log the values entered in the UI
        logger.debug("Adding new student - Id: {}, First Name: {}, Last Name: {}, Gender: {}, Email: {}",
                student.getId(), student.getFirstName(), student.getLastName(), student.getGender(), student.getEmail());

        studentRepository.save(student);
        logger.debug("Adding new student - Id: {}, First Name: {}, Last Name: {}, Gender: {}, Email: {}",
                student.getId(), student.getFirstName(), student.getLastName(), student.getGender(), student.getEmail());
        return "redirect:list";
    }


    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));

        model.addAttribute("student", student);
        logger.info("Updating student - Id: {}", student.getId());
        logger.debug("Previous student record: Id: {}, First Name: {}, Last Name: {}, Gender: {}, Email: {}",
                student.getId(), student.getFirstName(), student.getLastName(), student.getGender(), student.getEmail());
        return "update-student";
    }

    @PostMapping("update/{id}")
    public String updateStudent(@PathVariable("id") long id, @Valid Student student, BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            student.setId(id);
            logger.error("Error occurred while updating student: {}", result.getAllErrors());
            return "update-student";
        }

        // Log the values entered in the UI


        studentRepository.save(student);
        model.addAttribute("students", studentRepository.findAll());
        //logger.info("Updating student - Id: {}", student.getId());
        logger.debug("Updated student record: Id: {}, First Name: {}, Last Name: {}, Gender: {}, Email: {}",
                student.getId(), student.getFirstName(), student.getLastName(), student.getGender(), student.getEmail());
        return "index";
    }


    @GetMapping("delete/{id}")
    public String deleteStudent(@PathVariable("id") long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        studentRepository.delete(student);
        model.addAttribute("students", studentRepository.findAll());
        return "index";
    }


    @GetMapping("login")
    public String showLoginForm() {
        return "login"; // Display the login form
    }

    @PostMapping("login")
    public String login(@RequestParam String firstName, @RequestParam String password, Model model) {
        // Check if the provided first name exists in the database
        Student student = studentRepository.findByFirstName(firstName);

        // Validate the password if the student exists
        if (student != null && student.getPassword().equals(password)) {
            // Redirect to the welcome page if credentials match
            return "redirect:/students/welcome";
        } else {
            // Display error message for incorrect credentials
            model.addAttribute("errorMsg", "First name or password is incorrect");
            return "login"; // Return to the login form with an error message
        }
    }

    @GetMapping("welcome")
    public String showWelcomePage() {
        return "welcome"; // Display the welcome page
    }

    // Existing methods...

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Handling IllegalArgumentException: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMsg", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        logger.error("Handling Exception: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMsg", "An error occurred");
        return modelAndView;
    }
}