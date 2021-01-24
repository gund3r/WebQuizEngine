package engine.entities;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Component
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email
    @NotBlank
    @Size(min = 2, max = 254)
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Size(min = 5, max = 254)
    private String password;

    private String authorities;

    public User(){}

    public User(@Email @NotBlank @Size(min = 2, max = 254) String username,
                @NotBlank @Size(min = 5, max = 254) String password,
                String authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
}

