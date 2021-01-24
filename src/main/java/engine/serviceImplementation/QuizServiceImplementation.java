package engine.serviceImplementation;

import engine.repositories.CompletedQuizzesRepository;
import engine.repositories.QuizRepository;
import engine.service.QuizService;
import engine.entities.*;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
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

    final Logger log = org.slf4j.LoggerFactory.getLogger(QuizServiceImplementation.class);

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
        log.debug("Request to getAllQuizzes");
        return quizRepository.findAll(paging);
    }

    @Override
    public Page<CompletedQuiz> getAllCompletedQuizzes(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            Principal userPrincipal) {
        log.debug("Request to getAllCompletedQuizzes");
        String username = userPrincipal.getName();
        User user = userService.findByUsername(username);
        log.debug("Current user: {}", user);
        Long userId = user.getId();
        Pageable paging = PageRequest.of(pageNo, 10,
                Sort.by(Sort.Direction.DESC, "completedAt"));
        return completedQuizzesRepository.findAllCompletedQuizzesForUser(userId, paging);
    }

    @Override
    public Quiz getQuizById(final Long id) {
        log.debug("Request to getQuizById");
        return quizRepository.findById(id).orElseThrow();
    }

    @Override
    public Quiz addQuiz(final Quiz quiz) {
        log.debug("Request to addQuiz: {}", quiz);
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.findByUsername(currentUser);
        log.debug("Current user: {}", user);
        quiz.setUser(user);
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuizById(final Long id, final Quiz quizToUpdate) {
        log.debug("Request to updateQuizById: {}", quizToUpdate);
        Quiz quizFromDb = quizRepository.findById(id).get();
        log.debug("Quiz from repository: {}", quizFromDb);
        quizFromDb.setTitle(quizToUpdate.getTitle());
        quizFromDb.setText(quizToUpdate.getText());
        quizFromDb.setOptions(quizToUpdate.getOptions());
        quizFromDb.setAnswer(quizToUpdate.getAnswer());
        return quizRepository.save(quizFromDb);
    }

    @Override
    public void deleteQuizById(final Long id) {
        log.debug("Request to deleteQuizById: {}", id);
        quizRepository.deleteById(id);
    }

    @Override
    public Boolean isExist(Long id) {
        log.debug("Request to isExist: {}", id);
        return quizRepository.existsById(id);
    }

    @Override
    public Feedback solveTheQuiz(Long quizId, Answer answer, User user) {
        log.debug("Request to solveTheQuiz: quizId: {}, answer: {}, user: {}", quizId, answer, user);
        Feedback result = new Feedback(false);
        Quiz quiz = getQuizById(quizId);
        log.debug("Quiz from repository: {}", quiz);
        List<String> a = quiz.getAnswer();
        List<String> b = answer.getAnswer();
        log.debug("Answer from quiz in repository: {}", a.toString());
        log.debug("Answer from answer in arguments: {}", b.toString());
        if (b.equals(a)) {
            result = new Feedback(true);
            Long userId = user.getId();
            CompletedQuiz completedQuiz = completedQuizzesRepository.
                    save(new CompletedQuiz(quizId, userId));
            return result;
        }
        return result;
    }
}
