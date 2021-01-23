package engine.service;

import engine.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface QuizService {

    Page<Quiz> getAllQuizzes(Pageable paging);

    Page<CompletedQuiz> getAllCompletedQuizzes(Integer pageNo, Principal userPrincipal);

    Quiz getQuizById(Long id);

    Boolean isExist(Long id);

    Quiz addQuiz(Quiz quiz);

    Quiz updateQuizById(Long id, Quiz quizToUpdate);

    void deleteQuizById(Long id);

    Feedback solveTheQuiz(Long id, Answer answer, User user);

}
