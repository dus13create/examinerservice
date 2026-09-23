package skypro.examinerservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skypro.examinerservice.exception.QuestionNotFoundException;
import skypro.examinerservice.model.Question;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class JavaQuestionServiceTest {

    private JavaQuestionService out;

    @BeforeEach
    void setUp() {
        out = new JavaQuestionService();
    }

    @Test
    void shouldAddQuestionByStrings() {
        Question actual = out.add("Q1", "A1");
        Question expected = new Question("Q1", "A1");

        assertEquals(expected, actual);
        assertTrue(out.getAll().contains(expected));
    }

    @Test
    void shouldAddQuestionByObject() {
        Question question = new Question("Q1", "A1");
        Question actual = out.add(question);

        assertEquals(question, actual);
        assertEquals(1, out.getAll().size());
    }

    @Test
    void shouldRemoveExistingQuestion() {
        Question question = new Question("Q1", "A1");
        out.add(question);

        Question removed = out.remove(question);

        assertEquals(question, removed);
        assertTrue(out.getAll().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentQuestion() {
        Question question = new Question("Q_UNKNOWN", "A_UNKNOWN");

        assertThrows(QuestionNotFoundException.class, () -> out.remove(question));
    }

    @Test
    void shouldReturnAllQuestions() {
        Question q1 = out.add("Q1", "A1");
        Question q2 = out.add("Q2", "A2");

        Collection<Question> all = out.getAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(q1));
        assertTrue(all.contains(q2));
    }

    @Test
    void shouldReturnRandomQuestion() {
        Question q1 = out.add("Q1", "A1");
        Question q2 = out.add("Q2", "A2");

        Question random = out.getRandomQuestion();

        assertNotNull(random);
        assertTrue(random.equals(q1) || random.equals(q2));
    }

    @Test
    void shouldThrowExceptionWhenGetRandomFromEmptyList() {
        assertThrows(QuestionNotFoundException.class, () -> out.getRandomQuestion());
    }
}
