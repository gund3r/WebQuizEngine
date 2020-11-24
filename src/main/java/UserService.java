package main.java;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    Boolean isExist(Long id);

    User addUser(UserDTO userDTO);

    User updateUserById(Long id, User userToUpdate);

    void deleteUserById(Long id);

    List<Quiz> getQuizzesByUser(Long id);

}
