package skypro.examinerservice.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import skypro.examinerservice.exception.QuestionLimitExceededException;
import skypro.examinerservice.model.Question;

import java.util.*;

@Service
public class ExaminerServiceImpl implements ExaminerService {
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
        int totalQuestions = javaQuestionService.getAll().size() + mathQuestionService.getAll().size();
        if (amount <= 0 || amount > totalQuestions) {
            throw new QuestionLimitExceededException("Запрошено некорректное количество вопросов: " + amount);
        }

        Set<Question> examQuestions = new HashSet<>();
        while (examQuestions.size() < amount) {
            // Равновероятно берем случайный вопрос либо из Java, либо из Math
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

        return examQuestions;
    }
}

