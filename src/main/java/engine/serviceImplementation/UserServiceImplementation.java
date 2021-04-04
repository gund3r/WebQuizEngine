package engine.serviceImplementation;

import engine.repositories.UserRepository;
import engine.entities.User;
import engine.entities.UserDTO;
import engine.service.UserService;
import org.slf4j.Logger;
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
public class UserServiceImplementation implements UserDetailsService, UserService {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImplementation.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.debug("Request to loadUserByUsername: {}", userName);
        User user = findByUsername(userName);
        log.debug("User from repository: {}", user);
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
        log.debug("Request to findByUsername: {}", username);
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        log.debug("Request to getAllUsers");
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Long id) {
        log.debug("Request to getUserById: {}", id);
        return userRepository.findById(id).get();
    }

    public boolean isExist(Long id) {
        log.debug("Request to isExistUser: {}", id);
        return userRepository.existsById(id);
    }

    public User addUser(UserDTO userDTO) {
        log.debug("Request to addUser: {}", userDTO);
        if (userDTO == null) {
            return null;
        }
        String email = userDTO.getEmail();
        if (!isEmail(email)) {
            log.info("Bad format for email: {}", email);
            return null;
        }
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setUsername(email);
        newUser.setPassword(encryptedPassword);
        newUser.setAuthorities("USER");
        userRepository.save(newUser);
        return newUser;
    }

    public User updateUserById(Long id, User userToUpdate) {
        log.debug("Request to updateUserById: id: {}, userToUpdate: {}", id, userToUpdate);
        User userFromDb = userRepository.findById(id).get();
        log.debug("User from repository: {}", userFromDb);
        String encryptedPassword = passwordEncoder.encode(userToUpdate.getPassword());
        userFromDb.setUsername(userToUpdate.getUsername());
        userFromDb.setPassword(encryptedPassword);
        return userRepository.save(userFromDb);
    }

    public void deleteUserById(Long id) {
        log.debug("Request to deleteUserById: {}", id);
        userRepository.deleteById(id);
    }

    public boolean isEmail(String email) {
        log.debug("Request to isEmail: {}", email);
        if (email == null) {
            return false;
        }
        if (!email.contains("@")) {
            return false;
        }
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
