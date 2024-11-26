package uz.app.payload;

import lombok.Data;

@Data
public class SignUpDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int age;
}