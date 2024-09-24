package com.wavebank.wave.user;

import com.wavebank.wave.exception.UserAlreadyExistsException;
import com.wavebank.wave.registration.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    // Dependencies
    // UserRepository is an interface that extends JpaRepository used to interact with the database, it provides methods findAll(), findByEmail(), and save()
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Method used to retrieve all users from the database
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Method used to register a new user
    @Override
    public User registerUser(RegistrationRequest request) {

        // check if a user with provided email already exists or not using findByEmail method
        Optional<User> user = this.findByEmail(request.email());
        if (user.isPresent()) {
            // throw out UserAlreadyExistsException exception defined in exception package
            throw new UserAlreadyExistsException(
                    "User with email"+request.email() + "already exists");
        }

        // create a new user
        var newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        // password needs to be encoded
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());

        // save a new user to the database
        return userRepository.save(newUser);
    }
    // Method used to find a user by their email
    @Override
    public Optional<User> findByEmail(String email) {
        // Optional<User> is a container of object User which either has a non-null value or is empty
        return userRepository.findByEmail(email);
    }
}
