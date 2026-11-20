package skypro.examinerservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import skypro.examinerservice.model.Avatar;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Page<Avatar> findAll(Pageable pageable);
}
