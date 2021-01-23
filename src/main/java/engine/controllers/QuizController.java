package engine.controllers;

import engine.entities.*;
import engine.serviceImplementation.QuizServiceImplementation;
import engine.serviceImplementation.UserServiceImplementation;
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
public class QuizController {

    @Autowired
    private QuizServiceImplementation quizService;

    @Autowired
    private UserServiceImplementation userService;

    @GetMapping(path = "/quizzes", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Page<Quiz>> getAllQuizzes(@PageableDefault(sort = "id") Pageable pageable) {
        System.out.println("getAllQuizzes");
        Page<Quiz> quizzes = quizService.getAllQuizzes(pageable);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping(path = "/quizzes/completed", produces = {"application/json"})
    public ResponseEntity<Page<CompletedQuiz>> getAllCompletedQuizzes(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo, Principal userPrincipal) {
        System.out.println("getAllCompletedQuizzes");
        System.out.println(userPrincipal.getName());
        Page<CompletedQuiz> completedQuizzes = quizService.getAllCompletedQuizzes(pageNo, userPrincipal);
        return new ResponseEntity<>(completedQuizzes, HttpStatus.OK);
    }

    @GetMapping(path = "/quizzes/{quizId}", produces = "application/json")
    public ResponseEntity<Quiz> getQuizById(@PathVariable (value = "quizId") Long quizId) {
        System.out.println("getQuizById");
        if (!quizService.isExist(quizId)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Quiz quiz = quizService.getQuizById(quizId);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping(path = "/quizzes", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Quiz> addQuiz(@Valid @RequestBody Quiz quizToSave) {
        System.out.println("addQuiz");
        quizService.addQuiz(quizToSave);
        System.out.println(quizToSave.toString());
        return new ResponseEntity<>(quizToSave, HttpStatus.OK);
    }

    @PostMapping(path = "/quizzes/{quizId}/solve")
    @ResponseBody
    public ResponseEntity<Feedback> solveQuiz(@PathVariable (value = "quizId") Long quizId,
                                              @Valid @RequestBody Answer answer,
                                              Principal userPrincipal) {
        System.out.println("solveQuiz in the QuizController");
        System.out.println(answer.toString());
        System.out.println(userPrincipal.getName());
        String username = userPrincipal.getName();
        User user = userService.findByUsername(username);
        System.out.println("QuizServiceImplementation");
        Feedback result = quizService.solveTheQuiz(quizId, answer, user);
        System.out.println(result.toString());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/quizzes/{quizId}")
    @ResponseBody
    public ResponseEntity<String> deleteQuizById(@PathVariable (value = "quizId") Long quizId) {
        System.out.println("deleteQuizById");
        if (!quizService.isExist(quizId)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Quiz quizToDelete = quizService.getQuizById(quizId);
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        String quizUser = quizToDelete.getUser().getUsername();
        if (quizUser.equals(currentUser)) {
            quizService.deleteQuizById(quizId);
            return new ResponseEntity<>("The quiz is deleted!", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("The user is not owner", HttpStatus.FORBIDDEN);
    }

}
