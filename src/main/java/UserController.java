package main.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserServiceImplementation userServiceImplementation;

    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO) {
        if (!userServiceImplementation.isEmail(userDTO.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userServiceImplementation.findByUsername(userDTO.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userServiceImplementation.addUser(userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/users", produces = "application/json")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userServiceImplementation.getAllUsers(), HttpStatus.OK);
    }

    /**@GetMapping(path = "/users/{userId}/quizzes", produces = "application/json")
    public ResponseEntity<List<Quiz>> getAllQuizzesByUser(@PathVariable Long userId) {
    return new ResponseEntity<>(userServiceImplementation.getQuizzesByUser(userId), HttpStatus.OK);
    }**/

    @GetMapping(path = "/users/{userId}", produces = "application/json")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userServiceImplementation.getUserById(userId), HttpStatus.OK);
    }

    @PutMapping(path = "/users/{userId}", produces = "application/json")
    public ResponseEntity<User> updateUserById(@PathVariable Long userId, User userToUpdate) {
        return new ResponseEntity<>(userServiceImplementation.updateUserById(userId, userToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity deleteUserById(@PathVariable Long userId) {
        userServiceImplementation.deleteUserById(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/users/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/api/users";
    }

}
