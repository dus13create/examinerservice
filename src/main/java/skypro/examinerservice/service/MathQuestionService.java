package skypro.examinerservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import skypro.examinerservice.exception.QuestionNotFoundException;
import skypro.examinerservice.model.Question;
import skypro.examinerservice.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Service("mathQuestionService")
public class MathQuestionService implements QuestionService {
    private static final Logger log = LoggerFactory.getLogger(MathQuestionService.class);

    private final QuestionRepository questionRepository;
    private final Random random = new Random();

    public MathQuestionService(@Qualifier("mathQuestionRepository") QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question add(String question, String answer) {
        log.info("Was invoked method for add question: {}", question);
        return add(new Question(question, answer));
    }

    @Override
    public Question add(Question question) {
        log.info("Was invoked method for add question: {}", question);
        log.debug("Adding question to repository: {}", question);
        return questionRepository.add(question);
    }

    @Override
    public Question remove(Question question) {
        log.info("Was invoked method for remove question: {}", question);
        log.debug("Removing question from repository: {}", question);
        return questionRepository.remove(question);
    }

    @Override
    public Question update(Question question) {
        log.info("Was invoked method for update question: {}", question);
        log.debug("Updating question in repository: {}", question);
        return questionRepository.update(question);
    }

    @Override
    public Collection<Question> getAll() {
        log.info("Was invoked method for getAll");
        log.debug("Returning all questions, count: {}", questionRepository.getAll().size());
        return questionRepository.getAll();
    }

    @Override
    public Question getRandomQuestion() {
        log.info("Was invoked method for getRandomQuestion");
        Collection<Question> all = questionRepository.getAll();
        if (all.isEmpty()) {
            log.error("There is not student with id = {}. Math questions list is empty", 0);
            throw new QuestionNotFoundException("Список вопросов по математике пуст");
        }
        List<Question> questionList = new ArrayList<>(all);
        Question selected = questionList.get(random.nextInt(questionList.size()));
        log.debug("Random question selected: {}", selected);
        return selected;
    }
}
