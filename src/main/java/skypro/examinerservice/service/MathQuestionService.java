package skypro.examinerservice.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import skypro.examinerservice.exception.QuestionNotFoundException;
import skypro.examinerservice.model.Question;
import skypro.examinerservice.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Service
public class MathQuestionService implements QuestionService {
    private final QuestionRepository questionRepository;
    private final Random random = new Random();

    public MathQuestionService(@Qualifier("mathQuestionRepository") QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question add(String question, String answer) {
        return add(new Question(question, answer));
    }

    @Override
    public Question add(Question question) {
        return questionRepository.add(question);
    }

    @Override
    public Question remove(Question question) {
        return questionRepository.remove(question);
    }

    @Override
    public Question update(Question question) {
        return questionRepository.update(question);
    }

    @Override
    public Collection<Question> getAll() {
        return questionRepository.getAll();
    }

    @Override
    public Question getRandomQuestion() {
        Collection<Question> all = questionRepository.getAll();
        if (all.isEmpty()) {
            throw new QuestionNotFoundException("Список вопросов по математике пуст");
        }
        List<Question> questionList = new ArrayList<>(all);
        return questionList.get(random.nextInt(questionList.size()));
    }
}
