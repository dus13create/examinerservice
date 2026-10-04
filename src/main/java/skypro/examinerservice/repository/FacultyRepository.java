package skypro.examinerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skypro.examinerservice.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
