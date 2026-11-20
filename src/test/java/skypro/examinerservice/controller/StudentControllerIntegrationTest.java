package skypro.examinerservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import skypro.examinerservice.model.Faculty;
import skypro.examinerservice.model.Student;
import skypro.examinerservice.repository.FacultyRepository;
import skypro.examinerservice.repository.StudentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class StudentControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private Student savedStudent;
    private Faculty savedFaculty;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();

        savedFaculty = facultyRepository.save(new Faculty(null, "Engineering", "Blue", "Dr. Smith"));
        savedStudent = studentRepository.save(new Student(null, "John Doe", 20, "Gryffindor"));
    }

    @Test
    void getAllStudents() {
        ResponseEntity<List<Student>> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/student", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getStudentsByAgeBetween() {
        studentRepository.save(new Student(null, "Jane Doe", 22, "Slytherin"));

        ResponseEntity<List<Student>> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/student/age?min=20&max=21", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getStudentById() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/student/" + savedStudent.getId(), Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedStudent.getName(), response.getBody().getName());
    }

    @Test
    void getStudentByIdNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/student/9999", String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addStudent() {
        Student newStudent = new Student(null, "Alice", 21, "Hufflepuff");

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/exam/student", newStudent, Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("Alice", response.getBody().getName());
    }

    @Test
    void updateStudent() {
        Student updateBody = new Student(null, "John Updated", 25, "Ravenclaw");

        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:" + port + "/exam/student/" + savedStudent.getId(),
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updateBody),
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Updated", response.getBody().getName());
        assertEquals(25, response.getBody().getAge());
    }

    @Test
    void deleteStudent() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/exam/student/" + savedStudent.getId(),
                org.springframework.http.HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(studentRepository.findById(savedStudent.getId()).orElse(null));
    }

    @Test
    void getStudentFaculty() {
        savedStudent.setFaculty(savedFaculty);
        studentRepository.save(savedStudent);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/student/" + savedStudent.getId() + "/faculty", Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedFaculty.getName(), response.getBody().getName());
    }

    @Test
    void getStudentFacultyNotAssigned() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/student/" + savedStudent.getId() + "/faculty", String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
