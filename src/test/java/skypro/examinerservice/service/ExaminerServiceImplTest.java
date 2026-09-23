package skypro.examinerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skypro.examinerservice.exception.QuestionLimitExceededException;
import skypro.examinerservice.model.Question;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExaminerServiceImplTest {

    @Mock
    private QuestionService javaQuestionService;

    @Mock
    private QuestionService mathQuestionService;

    @InjectMocks
    private ExaminerServiceImpl out;

    private final List<Question> javaQuestions = List.of(new Question("JQ1", "JA1"));
    private final List<Question> mathQuestions = List.of(new Question("MQ1", "MA1"));

    @Test
    void shouldThrowExceptionWhenAmountExceedsTotalQuestions() {
        when(javaQuestionService.getAll()).thenReturn(javaQuestions);
        when(mathQuestionService.getAll()).thenReturn(mathQuestions);

        // Всего вопросов 2, запрашиваем 3
        assertThrows(QuestionLimitExceededException.class, () -> out.getQuestions(3));
    }

    @Test
    void shouldReturnCorrectNumberOfUniqueQuestions() {
        when(javaQuestionService.getAll()).thenReturn(javaQuestions);
        when(mathQuestionService.getAll()).thenReturn(mathQuestions);
        when(javaQuestionService.getRandomQuestion()).thenReturn(javaQuestions.get(0));
        when(mathQuestionService.getRandomQuestion()).thenReturn(mathQuestions.get(0));

        Collection<Question> actual = out.getQuestions(2);

        assertEquals(2, actual.size());
        assertTrue(actual.contains(javaQuestions.get(0)));
        assertTrue(actual.contains(mathQuestions.get(0)));
    }
}
