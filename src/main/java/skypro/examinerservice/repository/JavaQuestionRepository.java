package skypro.examinerservice.repository;

import org.springframework.stereotype.Repository;
import skypro.examinerservice.exception.QuestionNotFoundException;
import skypro.examinerservice.model.Question;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Repository
public class JavaQuestionRepository implements QuestionRepository {
    private final Set<Question> questions = new HashSet<>();

    @Override
    public Question add(Question question) {
        questions.add(question);
        return question;
    }

    @Override
    public Question remove(Question question) {
        if (!questions.remove(question)) {
            throw new QuestionNotFoundException("Вопрос не найден в репозитории Java");
        }
        return question;
    }

    @Override
    public Question update(Question question) {
        questions.remove(question);
        questions.add(question);
        return question;
    }

    @Override
    public Collection<Question> getAll() {
        return Collections.unmodifiableCollection(questions);
    }
}
