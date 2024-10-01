package com.wavebank.wave.event.listener;

import com.wavebank.wave.event.RegistrationCompleteEvent;
import com.wavebank.wave.user.User;
import com.wavebank.wave.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor

public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;
    //https://docs.spring.io/spring-boot/reference/io/email.html
    //private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User theUser;
        // 1. Get the newly registered user
        theUser = event.getUser();
        // 2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        // 3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser, verificationToken);
        // 4. Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        // 5. Send the email.
        /*
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
         */
        log.info("Click the link to verify your registration you have 5 minutes: {}",url);
    }
    // Does not work, figure out later
    // boot email account
    //https://stackoverflow.com/questions/35347269/javax-mail-authenticationfailedexception-535-5-7-8-username-and-password-not-ac
    /*
    1. Two Step Verification should be turned off.
    2. Allow Less Secure App(should be turned on).
    3. Please Check Your UserName and Password.
    */
    /*
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Wavebank";
        String mailContent = "<p> Hi, "+ theUser.getFirstName()+ ", </p>"+
                "<p>Thank you for registering to us,"+"" +
                "Please, follow the link below to complete your registration. </p>"+
                "<a href=\""+url+ "\">Verify ypur email to activate your account</a>"+
                "<p> Thank you <br> Wave";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wavebank782@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
    */
}
