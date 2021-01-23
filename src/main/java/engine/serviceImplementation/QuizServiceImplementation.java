package engine.serviceImplementation;

import engine.repositories.CompletedQuizzesRepository;
import engine.repositories.QuizRepository;
import engine.service.QuizService;
import engine.entities.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@NoArgsConstructor
@Component
@Service
public class QuizServiceImplementation implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CompletedQuizzesRepository completedQuizzesRepository;

    @Autowired
    private UserServiceImplementation userService;

    public QuizServiceImplementation(QuizRepository quizRepository, CompletedQuizzesRepository completedQuizzesRepository, UserServiceImplementation userService) {
        this.quizRepository = quizRepository;
        this.completedQuizzesRepository = completedQuizzesRepository;
        this.userService = userService;
    }

    @Override
    public Page<Quiz> getAllQuizzes(Pageable paging) {
        return quizRepository.findAll(paging);
    }

    @Override
    public Page<CompletedQuiz> getAllCompletedQuizzes(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            Principal userPrincipal) {
        String username = userPrincipal.getName();
        User user = userService.findByUsername(username);
        Long userId = user.getId();
        System.out.println(userId);
        Pageable paging = PageRequest.of(pageNo, 10,
                Sort.by(Sort.Direction.DESC, "completedAt"));
        System.out.println(completedQuizzesRepository.
                findAllCompletedQuizzesForUser(userId, paging).toList().toString());
        return completedQuizzesRepository.findAllCompletedQuizzesForUser(userId, paging);
    }

    @Override
    public Quiz getQuizById(final Long id) {
        return quizRepository.findById(id).orElseThrow();
    }

    @Override
    public Quiz addQuiz(final Quiz quiz) {
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.findByUsername(currentUser);
        quiz.setUser(user);
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuizById(final Long id, final Quiz quizToUpdate) {
        Quiz quizFromDb = quizRepository.findById(id).get();
        quizFromDb.setTitle(quizToUpdate.getTitle());
        quizFromDb.setText(quizToUpdate.getText());
        quizFromDb.setOptions(quizToUpdate.getOptions());
        quizFromDb.setAnswer(quizToUpdate.getAnswer());
        return quizRepository.save(quizFromDb);
    }

    @Override
    public void deleteQuizById(final Long id) {
        quizRepository.deleteById(id);
    }

    @Override
    public Boolean isExist(Long id) {
        return quizRepository.existsById(id);
    }

    @Override
    public Feedback solveTheQuiz(Long quizId, Answer answer, User user) {
        System.out.println("------------SolveTheQuiz------------");
        System.out.println("user = " + user.toString());
        System.out.println("answer = " + answer.toString());
        System.out.println("quizId = " + quizId);
        System.out.println("----------------------------------");
        Feedback result = new Feedback(false);
        Quiz quiz = getQuizById(quizId);
        List<String> a = quiz.getAnswer();
        List<String> b = answer.getAnswer();
        if (b.equals(a)) {
            result = new Feedback(true);
            System.out.println("b.equals(a)");
            Long userId = user.getId();
            System.out.println(userId);
            CompletedQuiz completedQuiz = completedQuizzesRepository.
                    save(new CompletedQuiz(quizId, userId));
            System.out.println(completedQuiz.toString());
            System.out.println(quiz.toString());
            System.out.println(user.toString());
            return result;
        }
        return result;
    }
}
