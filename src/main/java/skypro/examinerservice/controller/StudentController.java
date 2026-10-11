package skypro.examinerservice.controller;

import org.springframework.web.bind.annotation.*;
import skypro.examinerservice.model.Student;
import skypro.examinerservice.repository.StudentRepository;

import java.util.Collection;

@RestController
@RequestMapping("/exam/student")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/age")
    public Collection<Student> getStudentsByAgeBetween(
            @RequestParam("min") Integer min,
            @RequestParam("max") Integer max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable("id") Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable("id") Long id,
                                 @RequestBody Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());
        existingStudent.setHouse(student.getHouse());
        if (student.getFaculty() != null) {
            existingStudent.setFaculty(student.getFaculty());
        }
        return studentRepository.save(existingStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable("id") Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        studentRepository.delete(student);
    }

    @GetMapping("/{id}/faculty")
    public skypro.examinerservice.model.Faculty getStudentFaculty(@PathVariable("id") Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        if (student.getFaculty() == null) {
            throw new RuntimeException("Student does not belong to any faculty");
        }
        return student.getFaculty();
    }
}
