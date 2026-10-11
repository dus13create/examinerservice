package skypro.examinerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skypro.examinerservice.exception.QuestionNotFoundException;
import skypro.examinerservice.model.Question;
import skypro.examinerservice.repository.QuestionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MathQuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private MathQuestionService out;

    @Test
    void shouldAddQuestion() {
        Question question = new Question("2+2", "4");
        when(questionRepository.add(question)).thenReturn(question);

        Question actual = out.add(question);
        assertEquals(question, actual);
    }

    @Test
    void shouldGetRandomQuestion() {
        Question q1 = new Question("2+2", "4");
        when(questionRepository.getAll()).thenReturn(List.of(q1));

        Question actual = out.getRandomQuestion();
        assertEquals(q1, actual);
    }

    @Test
    void shouldThrowExceptionWhenRandomFromEmpty() {
        when(questionRepository.getAll()).thenReturn(List.of());

        assertThrows(QuestionNotFoundException.class, () -> out.getRandomQuestion());
    }
}
