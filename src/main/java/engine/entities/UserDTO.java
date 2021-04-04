package engine.entities;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDTO {

    @Email
    @Size(min = 4, max = 254)
    private String email;

    @NotBlank
    @Size(min = 5, max = 254)
    private String password;

    public UserDTO() { }

    public UserDTO(@Email @Size(min = 4, max = 254) String email,
                   @NotBlank @Size(min = 5, max = 254) String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO{"
                + "email='" + email + '\''
                + ", password='" + password + '\''
                + '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
