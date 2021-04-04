package engine.controllers;

import engine.entities.*;
import engine.serviceImplementation.QuizServiceImplementation;
import engine.serviceImplementation.UserServiceImplementation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public final class QuizController {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private QuizServiceImplementation quizService;

    @Autowired
    private UserServiceImplementation userService;

    @GetMapping(path = "/quizzes", produces = {"application/json"})
    public ResponseEntity<Page<Quiz>> getAllQuizzes(@PageableDefault(sort = "id") Pageable pageable) {
        log.debug("REST request to getAllQuizzes");
        Page<Quiz> quizzes = quizService.getAllQuizzes(pageable);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping(path = "/quizzes/completed", produces = {"application/json"})
    public ResponseEntity<Page<CompletedQuiz>> getAllCompletedQuizzes(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo, Principal userPrincipal) {
        log.debug("REST request to getAllCompletedQuizzesByUser");
        Page<CompletedQuiz> completedQuizzes = quizService.getAllCompletedQuizzes(pageNo, userPrincipal);
        return new ResponseEntity<>(completedQuizzes, HttpStatus.OK);
    }

    @GetMapping(path = "/quizzes/{quizId}", produces = "application/json")
    public ResponseEntity<Quiz> getQuizById(@PathVariable (value = "quizId") Long quizId) {
        log.debug("REST request to getQuizById: {}", quizId);
        if (!quizService.isExist(quizId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Quiz quiz = quizService.getQuizById(quizId);
        log.debug("Quiz to return: {}", quiz);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping(path = "/quizzes", consumes = "application/json")
    public ResponseEntity<Quiz> addQuiz(@Valid @RequestBody Quiz quizToSave) {
        log.debug("REST request to addQuiz: {}", quizToSave);
        quizService.addQuiz(quizToSave);
        return new ResponseEntity<>(quizToSave, HttpStatus.OK);
    }

    @PostMapping(path = "/quizzes/{quizId}/solve")
    public ResponseEntity<Feedback> solveQuiz(@PathVariable (value = "quizId") Long quizId,
                                              @Valid @RequestBody Answer answer,
                                              Principal userPrincipal) {
        log.debug("REST request to solveQuiz: {}", answer);
        String username = userPrincipal.getName();
        log.debug("Get username from authentication: {}", username);
        User user = userService.findByUsername(username);
        log.debug("Get user from repository: {}", user);
        Feedback result = quizService.solveTheQuiz(quizId, answer, user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/quizzes/{quizId}")
    public ResponseEntity<String> deleteQuizById(@PathVariable (value = "quizId") Long quizId) {
        log.debug("REST request to deleteQuizById: {}", quizId);
        if (!quizService.isExist(quizId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Quiz quizToDelete = quizService.getQuizById(quizId);
        log.debug("Get quiz from repository: {}", quizToDelete);
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        String quizUser = quizToDelete.getUser().getUsername();
        log.debug("currentUser: {}, quizUser: {}", currentUser, quizUser);
        if (quizUser.equals(currentUser)) {
            quizService.deleteQuizById(quizId);
            return new ResponseEntity<>("The quiz is deleted!", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("The user is not owner", HttpStatus.FORBIDDEN);
    }

}
