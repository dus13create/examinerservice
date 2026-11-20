package skypro.examinerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import skypro.examinerservice.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAgeBetween(Integer min, Integer max);

    @Query("SELECT COUNT(*) FROM Student")
    long countAll();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double findAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findTop5ByOrderByDescId();
}
