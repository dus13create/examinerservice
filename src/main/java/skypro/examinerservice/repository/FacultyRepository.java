package skypro.examinerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import skypro.examinerservice.model.Faculty;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Query("SELECT f FROM Faculty f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(f.color) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Collection<Faculty> findByNameOrColorIgnoreCase(String searchTerm);
}
