package skypro.examinerservice.service;

import skypro.examinerservice.model.Question;

import java.util.Collection;

public interface ExaminerService {
    Collection<Question> getQuestions(int amount);
}
