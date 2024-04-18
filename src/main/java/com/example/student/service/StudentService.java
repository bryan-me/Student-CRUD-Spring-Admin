package com.example.student.service;

import com.example.student.model.Student;

import java.util.List;

public interface StudentService {

    Student addStudent(Student student);
    List<Student> getAllStudents();
    Student getStudentById(long id);
    void updateStudent(long id, Student student);
    void deleteStudent(long id);
    Student findByFirstName(String firstName);

}
