package main.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class UserServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: " + userName);
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getAuthorities())
                .build();
        return userDetails;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    public Boolean isExist(Long id) {
        return userRepository.existsById(id);
    }

    public User addUser(UserDTO userDTO) {
        if (userDTO == null) return null;
        String email = userDTO.getEmail();
        if (!isEmail(email)) {
            System.out.println("Bad format for email");
            return null;
        }
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setUsername(email);
        System.out.println(userDTO.getEmail());
        newUser.setPassword(encryptedPassword);
        System.out.println(newUser.toString());
        newUser.setAuthorities("USER");
        userRepository.save(newUser);
        System.out.println(newUser.toString());
        return newUser;
    }

    public User updateUserById(Long id, User userToUpdate) {
        User userFromDb = userRepository.findById(id).get();
        String encryptedPassword = passwordEncoder.encode(userToUpdate.getPassword());
        userFromDb.setUsername(userToUpdate.getUsername());
        userFromDb.setPassword(encryptedPassword);
        return userRepository.save(userFromDb);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    /**public List<Quiz> getQuizzesByUser(Long id) {
     User user = getUserById(id);
     return user.getQuizzes();
     }*/

    public boolean isEmail(String email) {
        if (email == null) return false;
        if (!email.contains("@")) return false;
        char[] charArray = email.toCharArray();
        String result = "";
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '@') {
                result = email.substring(i);
            }
        }
        if (result.contains(".")) {
            return true;
        }
        return false;
    }

}
