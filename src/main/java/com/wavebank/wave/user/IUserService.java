package com.wavebank.wave.user;

import com.wavebank.wave.registration.RegistrationRequest;
import com.wavebank.wave.registration.token.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    //Method to return a list of all users
    List<User> getUsers();
    //Method to register a new user using RegistrationRequest object containing user details and return a newly registered User
    User registerUser(RegistrationRequest request);
    //Method to return an Optional<User> object containing user if they're found by email or Optional.empty() if no user with that email exists
    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);

    String deleteToken(String theToken);
}
