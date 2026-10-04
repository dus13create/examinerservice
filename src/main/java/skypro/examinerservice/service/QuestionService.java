package skypro.examinerservice.service;

import skypro.examinerservice.model.Question;

import java.util.Collection;

public interface QuestionService {
    Question add(String question, String answer);

    Question add(Question question);

    Question remove(Question question);

    Question update(Question question);

    Collection<Question> getAll();

    Question getRandomQuestion();
}
