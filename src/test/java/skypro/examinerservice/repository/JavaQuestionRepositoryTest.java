package skypro.examinerservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skypro.examinerservice.exception.QuestionNotFoundException;
import skypro.examinerservice.model.Question;

import static org.junit.jupiter.api.Assertions.*;

class JavaQuestionRepositoryTest {
    private JavaQuestionRepository out;

    @BeforeEach
    void setUp() {
        out = new JavaQuestionRepository();
    }

    @Test
    void shouldAddQuestion() {
        Question q = new Question("Q1", "A1");
        Question actual = out.add(q);

        assertEquals(q, actual);
        assertTrue(out.getAll().contains(q));
    }

    @Test
    void shouldRemoveQuestion() {
        Question q = new Question("Q1", "A1");
        out.add(q);

        Question removed = out.remove(q);

        assertEquals(q, removed);
        assertTrue(out.getAll().isEmpty());
    }

    @Test
    void shouldThrowWhenRemovingNonExistent() {
        assertThrows(QuestionNotFoundException.class, () -> out.remove(new Question("Q", "A")));
    }
}
