package skypro.examinerservice.controller;

import org.springframework.web.bind.annotation.*;
import skypro.examinerservice.model.Faculty;
import skypro.examinerservice.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/exam/faculty")
public class FacultyController {

    private final FacultyRepository facultyRepository;

    public FacultyController(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @GetMapping
    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @GetMapping("/search")
    public Collection<Faculty> searchFaculties(@RequestParam("term") String term) {
        return facultyRepository.findByNameOrColorIgnoreCase(term);
    }

    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable("id") Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable("id") Long id,
                                 @RequestBody Faculty faculty) {
        Faculty existingFaculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));

        existingFaculty.setName(faculty.getName());
        existingFaculty.setColor(faculty.getColor());
        existingFaculty.setHead(faculty.getHead());
        return facultyRepository.save(existingFaculty);
    }

    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable("id") Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
        facultyRepository.delete(faculty);
    }

    @GetMapping("/{id}/students")
    public List<skypro.examinerservice.model.Student> getFacultyStudents(@PathVariable("id") Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
        return faculty.getStudents();
    }
}
