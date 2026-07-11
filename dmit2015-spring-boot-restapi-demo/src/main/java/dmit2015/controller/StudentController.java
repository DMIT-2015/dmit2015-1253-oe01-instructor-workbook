package dmit2015.controller;

import dmit2015.entity.Student;
import dmit2015.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<Student> getStudents() {
        return  studentRepository.findAll();
    }

    @GetMapping("{id}")
    public Student  getStudent(@PathVariable Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Student> createNewStudent(@RequestBody Student student) {
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(student);

    }
}

