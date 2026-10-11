package skypro.examinerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skypro.examinerservice.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAgeBetween(Integer min, Integer max);
}
