package com.wavebank.wave.user;

import com.wavebank.wave.exception.UserAlreadyExistsException;
import com.wavebank.wave.registration.RegistrationRequest;
import com.wavebank.wave.registration.token.VerificationToken;
import com.wavebank.wave.registration.token.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    // Dependencies
    // UserRepository is an interface that extends JpaRepository used to interact with the database, it provides methods findAll(), findByEmail(), and save()
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    // Method used to retrieve all users from the database
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

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
    @Override
    public Optional<User> findByEmail(String email) {
        // Optional<User> is a container of object User which either has a non-null value or is empty
        return userRepository.findByEmail(email);
    }
    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null) {
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepository.delete(token);
            return "Token expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";

    }

    @Override
    public String deleteToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null) {
            return "Token not found";
        }
        tokenRepository.delete(token);

        return "Token deleted";
    }
}
