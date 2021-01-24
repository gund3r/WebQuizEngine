package engine.service;

import engine.entities.User;
import engine.entities.UserDTO;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    boolean isExist(Long id);

    User addUser(UserDTO userDTO);

    User updateUserById(Long id, User userToUpdate);

    void deleteUserById(Long id);

}
