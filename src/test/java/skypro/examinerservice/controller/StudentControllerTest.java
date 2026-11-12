package skypro.examinerservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import skypro.examinerservice.model.Faculty;
import skypro.examinerservice.model.Student;
import skypro.examinerservice.repository.StudentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    void getAllStudents() throws Exception {
        Student student1 = new Student(1L, "John Doe", 20, "Gryffindor");
        Student student2 = new Student(2L, "Jane Doe", 22, "Slytherin");
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/exam/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(studentRepository).findAll();
    }

    @Test
    void getStudentsByAgeBetween() throws Exception {
        Student student = new Student(1L, "John Doe", 20, "Gryffindor");
        when(studentRepository.findByAgeBetween(18, 25)).thenReturn(List.of(student));

        mockMvc.perform(get("/exam/student/age").param("min", "18").param("max", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(studentRepository).findByAgeBetween(18, 25);
    }

    @Test
    void getStudentById() throws Exception {
        Student student = new Student(1L, "John Doe", 20, "Gryffindor");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/exam/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.house").value("Gryffindor"));

        verify(studentRepository).findById(1L);
    }

    @Test
    void getStudentByIdNotFound() throws Exception {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/exam/student/999"))
                .andExpect(status().isInternalServerError());

        verify(studentRepository).findById(999L);
    }

    @Test
    void addStudent() throws Exception {
        Student student = new Student(null, "Alice", 21, "Hufflepuff");
        Student saved = new Student(1L, "Alice", 21, "Hufflepuff");
        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        mockMvc.perform(post("/exam/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"age\":21,\"house\":\"Hufflepuff\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudent() throws Exception {
        Student existing = new Student(1L, "John Doe", 20, "Gryffindor");
        Student updated = new Student(1L, "John Updated", 25, "Ravenclaw");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(Student.class))).thenReturn(updated);

        mockMvc.perform(put("/exam/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Updated\",\"age\":25,\"house\":\"Ravenclaw\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.age").value(25));

        verify(studentRepository).findById(1L);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void deleteStudent() throws Exception {
        Student student = new Student(1L, "John Doe", 20, "Gryffindor");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(delete("/exam/student/1"))
                .andExpect(status().isOk());

        verify(studentRepository).findById(1L);
        verify(studentRepository).delete(student);
    }

    @Test
    void deleteStudentNotFound() throws Exception {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/exam/student/999"))
                .andExpect(status().isInternalServerError());

        verify(studentRepository).findById(999L);
    }

    @Test
    void getStudentFaculty() throws Exception {
        Student student = new Student(1L, "John Doe", 20, "Gryffindor");
        Faculty faculty = new Faculty(1L, "Engineering", "Blue", "Dr. Smith");
        student.setFaculty(faculty);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/exam/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Engineering"))
                .andExpect(jsonPath("$.color").value("Blue"));

        verify(studentRepository).findById(1L);
    }

    @Test
    void getStudentFacultyNotAssigned() throws Exception {
        Student student = new Student(1L, "John Doe", 20, "Gryffindor");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/exam/student/1/faculty"))
                .andExpect(status().isInternalServerError());

        verify(studentRepository).findById(1L);
    }
}
