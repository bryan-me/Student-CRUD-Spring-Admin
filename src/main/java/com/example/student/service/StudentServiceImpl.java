package com.example.student.service;

import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    public final StudentRepository studentRepository;

    @Autowired
    public  StudentServiceImpl(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }
    @Override
    public Student addStudent(Student student) {

        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(long id) {
        return studentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid Student Id:" + id));
    }

    @Override
    public void updateStudent(long id, Student student) {
        if(studentRepository.existsById(id)){
            student.setId(id);
            studentRepository.save(student);
        }else{
            throw new IllegalArgumentException("Invalid student Id:" + id);
        }
    }

    @Override
    public void deleteStudent(long id) {
if(studentRepository.existsById(id)){
    studentRepository.deleteById(id);
}else{
    throw new IllegalArgumentException("Invalid student Id:" + id);
}
    }

    @Override
    public Student findByFirstName(String firstName) {
        return studentRepository.findByFirstName(firstName);
    }
}
