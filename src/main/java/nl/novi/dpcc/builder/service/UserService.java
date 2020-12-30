package nl.novi.dpcc.builder.service;

import nl.novi.dpcc.builder.domain.User;
import nl.novi.dpcc.builder.payload.request.UserRegistrationRequest;
import nl.novi.dpcc.builder.payload.response.MessageResponse;
import nl.novi.dpcc.builder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class UserService {

//    private UserRepository userRepository;

    @Autowired
    private UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<MessageResponse>  registerUser(@Valid UserRegistrationRequest userRegistrationRequest) {

        if (Boolean.TRUE.equals(userRepository.existsByUsername(userRegistrationRequest.getUsername()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(userRegistrationRequest.getEmail()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (Boolean.TRUE.equals(userRegistrationRequest.getPassword()==userRegistrationRequest.getPasswordRepeated())){

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Passwords provided are not the same"));
        }


        System.out.println(userRegistrationRequest.getPassword()==userRegistrationRequest.getPasswordRepeated());
        // TODO Student: Bonus: Check if wachtwoorden gelijk zijn. Geef anders een error.
        // TODO Student: Hier moet het userRegistrationRequest object omgebouwd worden naar een User-object!
        // TODO Student: Huidige code laat een leeg user object op in de database.

        try {
            User newUser = new User();
            newUser.setUsername(userRegistrationRequest.getUsername());
            newUser.setEmail(userRegistrationRequest.getEmail());
            newUser.setPassword(userRegistrationRequest.getPassword());
            newUser.setFirstName(userRegistrationRequest.getFirstName());
            newUser.setLastName(userRegistrationRequest.getLastName());
            userRepository.save(newUser);
        } catch (Exception e){
            throw new RuntimeException();
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
