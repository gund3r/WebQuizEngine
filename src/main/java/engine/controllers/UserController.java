package engine.controllers;

import engine.entities.User;
import engine.entities.UserDTO;
import engine.serviceImplementation.UserServiceImplementation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserServiceImplementation userServiceImplementation;

    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to addUser: {}", userDTO);
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
        log.debug("REST request to getAllUsers");
        return new ResponseEntity<>(userServiceImplementation.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(path = "/users/{userId}", produces = "application/json")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        log.debug("REST request to getUserById: {}", userId);
        return new ResponseEntity<>(userServiceImplementation.getUserById(userId), HttpStatus.OK);
    }

    @PutMapping(path = "/users/{userId}", produces = "application/json")
    public ResponseEntity<User> updateUserById(@PathVariable Long userId, User userToUpdate) {
        log.debug("REST request to updateUserById: {} {}", userId, userToUpdate);
        return new ResponseEntity<>(userServiceImplementation.updateUserById(userId, userToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity deleteUserById(@PathVariable Long userId) {
        log.debug("REST request to deleteUserById: {}", userId);
        userServiceImplementation.deleteUserById(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
