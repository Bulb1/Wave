package com.wavebank.wave.registration;

import com.wavebank.wave.event.RegistrationCompleteEvent;
import com.wavebank.wave.registration.token.VerificationToken;
import com.wavebank.wave.registration.token.VerificationTokenRepository;
import com.wavebank.wave.user.User;
import com.wavebank.wave.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

//https://projectlombok.org/features/
//this class will handle web requests and return data as a response
@RestController
@RequiredArgsConstructor
//Maps all requests with the URL /register to this controller, a base URL in this controller
@RequestMapping("/register")

public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    //shortcut for @RequestMapping(method = RequestMethod.POST)
    //https://en.wikipedia.org/wiki/POST_(HTTP)
    @PostMapping

    /* Endpoint Method
        RegistrationRequest registrationRequest: object is used to capture the incoming user registration data
        HttpServletRequest request: object allows the server to access details about
        the request, like the server's hostname, port, and context path, which are needed to generate the application URL.
     */
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        //creates a new user in database and returns it
        User user = userService.registerUser(registrationRequest);
        // publishes registration event once the user is registered and triggers sending of a verification email
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));

        return "Success! Please, check your email for verification link.";

    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token")String token) {
        VerificationToken theToken = tokenRepository.findByToken(token);
        //check if the user is already enabled
        if(theToken == null) {
            return "Invalid verification token";
        }
        if (theToken.getUser().isEnabled()) {
            return "This account has already been verified, please, login.";
        }
        //Validate token
        String verificationResult = userService.validateToken(token);

        if(verificationResult.equalsIgnoreCase("valid")) {
            userService.deleteToken(token);
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification token";
    }

    // Method used to generate a verification URL that's passed to RegistrationCompleteEvent
    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

}
