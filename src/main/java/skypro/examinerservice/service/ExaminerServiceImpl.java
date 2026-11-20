package skypro.examinerservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import skypro.examinerservice.exception.QuestionLimitExceededException;
import skypro.examinerservice.model.Question;

import java.util.*;

@Service
public class ExaminerServiceImpl implements ExaminerService {
    private static final Logger log = LoggerFactory.getLogger(ExaminerServiceImpl.class);

    private final QuestionService javaQuestionService;
    private final QuestionService mathQuestionService;
    private final Random random = new Random();

    public ExaminerServiceImpl(@Qualifier("javaQuestionService") QuestionService javaQuestionService,
                               @Qualifier("mathQuestionService") QuestionService mathQuestionService) {
        this.javaQuestionService = javaQuestionService;
        this.mathQuestionService = mathQuestionService;
    }

    @Override
    public Collection<Question> getQuestions(int amount) {
        log.info("Was invoked method for getQuestions with amount: {}", amount);
        log.debug("Getting questions - java: {}, math: {}",
                javaQuestionService.getAll().size(),
                mathQuestionService.getAll().size());

        int totalQuestions = javaQuestionService.getAll().size() + mathQuestionService.getAll().size();
        if (amount <= 0 || amount > totalQuestions) {
            log.error("Invalid question amount requested: {}. Total available: {}", amount, totalQuestions);
            throw new QuestionLimitExceededException("Запрошено некорректное количество вопросов: " + amount);
        }

        Set<Question> examQuestions = new HashSet<>();
        while (examQuestions.size() < amount) {
            if (random.nextBoolean()) {
                if (!javaQuestionService.getAll().isEmpty()) {
                    examQuestions.add(javaQuestionService.getRandomQuestion());
                }
            } else {
                if (!mathQuestionService.getAll().isEmpty()) {
                    examQuestions.add(mathQuestionService.getRandomQuestion());
                }
            }
        }

        log.info("getQuestions completed. Returned {} questions", examQuestions.size());
        return examQuestions;
    }
}

