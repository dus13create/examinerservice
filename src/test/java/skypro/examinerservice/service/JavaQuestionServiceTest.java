package skypro.examinerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skypro.examinerservice.exception.QuestionNotFoundException;
import skypro.examinerservice.model.Question;
import skypro.examinerservice.repository.QuestionRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JavaQuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private JavaQuestionService out;

    @Test
    void shouldAddQuestionByStrings() {
        Question question = new Question("Q1", "A1");
        when(questionRepository.add(question)).thenReturn(question);

        Question actual = out.add("Q1", "A1");
        Question expected = new Question("Q1", "A1");

        assertEquals(expected, actual);
    }

    @Test
    void shouldAddQuestionByObject() {
        Question question = new Question("Q1", "A1");
        when(questionRepository.add(question)).thenReturn(question);

        Question actual = out.add(question);

        assertEquals(question, actual);
    }

    @Test
    void shouldRemoveExistingQuestion() {
        Question question = new Question("Q1", "A1");
        when(questionRepository.remove(question)).thenReturn(question);

        Question removed = out.remove(question);

        assertEquals(question, removed);
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentQuestion() {
        Question question = new Question("Q_UNKNOWN", "A_UNKNOWN");
        when(questionRepository.remove(question)).thenThrow(new QuestionNotFoundException("Вопрос не найден"));

        assertThrows(QuestionNotFoundException.class, () -> out.remove(question));
    }

    @Test
    void shouldReturnAllQuestions() {
        Question q1 = new Question("Q1", "A1");
        Question q2 = new Question("Q2", "A2");
        when(questionRepository.getAll()).thenReturn(List.of(q1, q2));

        Collection<Question> all = out.getAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(q1));
        assertTrue(all.contains(q2));
    }

    @Test
    void shouldReturnRandomQuestion() {
        Question q1 = new Question("Q1", "A1");
        Question q2 = new Question("Q2", "A2");
        when(questionRepository.getAll()).thenReturn(List.of(q1, q2));

        Question random = out.getRandomQuestion();

        assertNotNull(random);
        assertTrue(random.equals(q1) || random.equals(q2));
    }

    @Test
    void shouldThrowExceptionWhenGetRandomFromEmptyList() {
        when(questionRepository.getAll()).thenReturn(List.of());

        assertThrows(QuestionNotFoundException.class, () -> out.getRandomQuestion());
    }
}
