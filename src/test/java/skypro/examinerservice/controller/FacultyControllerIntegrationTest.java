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
class FacultyControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Faculty savedFaculty;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();

        savedFaculty = facultyRepository.save(new Faculty(null, "Engineering", "Blue", "Dr. Smith"));
    }

    @Test
    void getAllFaculties() {
        ResponseEntity<List<Faculty>> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/faculty", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void searchFaculties() {
        facultyRepository.save(new Faculty(null, "Science", "Green", "Dr. Jones"));

        ResponseEntity<List<Faculty>> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/faculty/search?term=engine", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().stream().anyMatch(f -> "Engineering".equals(f.getName())));
    }

    @Test
    void getFacultyById() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/faculty/" + savedFaculty.getId(), Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedFaculty.getName(), response.getBody().getName());
    }

    @Test
    void getFacultyByIdNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/faculty/9999", String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addFaculty() {
        Faculty newFaculty = new Faculty(null, "Arts", "Red", "Dr. Brown");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/exam/faculty", newFaculty, Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("Arts", response.getBody().getName());
    }

    @Test
    void updateFaculty() {
        Faculty updateBody = new Faculty(null, "Engineering Updated", "Purple", "Dr. White");

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/exam/faculty/" + savedFaculty.getId(),
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updateBody),
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Engineering Updated", response.getBody().getName());
        assertEquals("Purple", response.getBody().getColor());
    }

    @Test
    void deleteFaculty() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/exam/faculty/" + savedFaculty.getId(),
                org.springframework.http.HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(facultyRepository.findById(savedFaculty.getId()).orElse(null));
    }

    @Test
    void getFacultyStudents() {
        Student student = studentRepository.save(new Student(null, "John Doe", 20, "Gryffindor"));
        student.setFaculty(savedFaculty);
        studentRepository.save(student);

        ResponseEntity<List<Student>> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/faculty/" + savedFaculty.getId() + "/students", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getFacultyStudentsNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/exam/faculty/9999/students", String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
