package skypro.examinerservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skypro.examinerservice.model.Avatar;
import skypro.examinerservice.repository.AvatarRepository;

@RestController
@RequestMapping("/exam/avatar")
public class AvatarTController {

    private final AvatarRepository avatarRepository;

    public AvatarTController(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @GetMapping
    public Page<Avatar> getAllAvatars(Pageable pageable) {
        return avatarRepository.findAll(pageable);
    }
}
