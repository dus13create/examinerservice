package skypro.examinerservice.controller;

import org.springframework.web.bind.annotation.*;
import skypro.examinerservice.model.Student;
import skypro.examinerservice.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/count")
    public long countAllStudents() {
        return studentRepository.countAll();
    }

    @GetMapping("/average-age")
    public Double getAverageAge() {
        Double avg = studentRepository.findAverageAge();
        return avg != null ? avg : 0.0;
    }

    @GetMapping("/last-5")
    public List<Student> getLast5Students() {
        return studentRepository.findTop5ByOrderByDescId();
    }

    @GetMapping("/names-starting-with-a")
    public List<String> getNamesStartingWithA() {
        return studentRepository.findAll().stream()
                .filter(s -> s.getName() != null && s.getName().startsWith("A"))
                .map(Student::getName)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/average-age-stream")
    public Double getAverageAgeStream() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return 0.0;
        }
        return students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @GetMapping("/print-parallel")
    public String printParallel() {
        List<Student> students = studentRepository.findAll();

        // Первые два имени — в основном потоке
        if (students.size() > 0) System.out.println(Thread.currentThread().getName() + ": " + students.get(0).getName());
        if (students.size() > 1) System.out.println(Thread.currentThread().getName() + ": " + students.get(1).getName());

        // Третий и четвертый — в параллельном потоке
        Thread thread1 = new Thread(() -> {
            if (students.size() > 2) System.out.println(Thread.currentThread().getName() + ": " + students.get(2).getName());
            if (students.size() > 3) System.out.println(Thread.currentThread().getName() + ": " + students.get(3).getName());
        });
        thread1.start();

        // Пятый и шестой — в ещё одном параллельном потоке
        Thread thread2 = new Thread(() -> {
            if (students.size() > 4) System.out.println(Thread.currentThread().getName() + ": " + students.get(4).getName());
            if (students.size() > 5) System.out.println(Thread.currentThread().getName() + ": " + students.get(5).getName());
        });
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "Names printed in parallel";
    }

    private synchronized void printName(String name) {
        System.out.println(Thread.currentThread().getName() + ": " + name);
    }

    @GetMapping("/print-synchronized")
    public String printSynchronized() {
        List<Student> students = studentRepository.findAll();

        // Первые два имени — в основном потоке
        if (students.size() > 0) printName(students.get(0).getName());
        if (students.size() > 1) printName(students.get(1).getName());

        // Третий и четвертый — в параллельном потоке
        Thread thread1 = new Thread(() -> {
            if (students.size() > 2) printName(students.get(2).getName());
            if (students.size() > 3) printName(students.get(3).getName());
        });
        thread1.start();

        // Пятый и шестой — в ещё одном параллельном потоке
        Thread thread2 = new Thread(() -> {
            if (students.size() > 4) printName(students.get(4).getName());
            if (students.size() > 5) printName(students.get(5).getName());
        });
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "Names printed with synchronization";
    }
}
