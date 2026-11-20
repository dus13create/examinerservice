package skypro.examinerservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import skypro.examinerservice.model.Faculty;
import skypro.examinerservice.model.Student;
import skypro.examinerservice.repository.FacultyRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @Test
    void getAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty(1L, "Engineering", "Blue", "Dr. Smith");
        Faculty faculty2 = new Faculty(2L, "Science", "Green", "Dr. Jones");
        when(facultyRepository.findAll()).thenReturn(Arrays.asList(faculty1, faculty2));

        mockMvc.perform(get("/exam/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Engineering"))
                .andExpect(jsonPath("$[1].name").value("Science"));

        verify(facultyRepository).findAll();
    }

    @Test
    void searchFaculties() throws Exception {
        Faculty faculty = new Faculty(1L, "Engineering", "Blue", "Dr. Smith");
        when(facultyRepository.findByNameOrColorIgnoreCase("engine")).thenReturn(List.of(faculty));

        mockMvc.perform(get("/exam/faculty/search").param("term", "engine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Engineering"));

        verify(facultyRepository).findByNameOrColorIgnoreCase("engine");
    }

    @Test
    void getFacultyById() throws Exception {
        Faculty faculty = new Faculty(1L, "Engineering", "Blue", "Dr. Smith");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/exam/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Engineering"))
                .andExpect(jsonPath("$.color").value("Blue"));

        verify(facultyRepository).findById(1L);
    }

    @Test
    void getFacultyByIdNotFound() throws Exception {
        when(facultyRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/exam/faculty/999"))
                .andExpect(status().isInternalServerError());

        verify(facultyRepository).findById(999L);
    }

    @Test
    void addFaculty() throws Exception {
        Faculty faculty = new Faculty(null, "Arts", "Red", "Dr. Brown");
        Faculty saved = new Faculty(1L, "Arts", "Red", "Dr. Brown");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(saved);

        mockMvc.perform(post("/exam/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Arts\",\"color\":\"Red\",\"head\":\"Dr. Brown\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Arts"));

        verify(facultyRepository).save(any(Faculty.class));
    }

    @Test
    void updateFaculty() throws Exception {
        Faculty existing = new Faculty(1L, "Engineering", "Blue", "Dr. Smith");
        Faculty updated = new Faculty(1L, "Engineering Updated", "Purple", "Dr. White");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updated);

        mockMvc.perform(put("/exam/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Engineering Updated\",\"color\":\"Purple\",\"head\":\"Dr. White\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Engineering Updated"))
                .andExpect(jsonPath("$.color").value("Purple"));

        verify(facultyRepository).findById(1L);
        verify(facultyRepository).save(any(Faculty.class));
    }

    @Test
    void deleteFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Engineering", "Blue", "Dr. Smith");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(delete("/exam/faculty/1"))
                .andExpect(status().isOk());

        verify(facultyRepository).findById(1L);
        verify(facultyRepository).delete(faculty);
    }

    @Test
    void deleteFacultyNotFound() throws Exception {
        when(facultyRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/exam/faculty/999"))
                .andExpect(status().isInternalServerError());

        verify(facultyRepository).findById(999L);
    }

    @Test
    void getFacultyStudents() throws Exception {
        Faculty faculty = new Faculty(1L, "Engineering", "Blue", "Dr. Smith");
        Student student = new Student(1L, "John Doe", 20, "Gryffindor");
        faculty.setStudents(List.of(student));
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/exam/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(facultyRepository).findById(1L);
    }

    @Test
    void getFacultyStudentsNotFound() throws Exception {
        when(facultyRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/exam/faculty/999/students"))
                .andExpect(status().isInternalServerError());

        verify(facultyRepository).findById(999L);
    }
}
