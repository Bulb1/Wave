package com.wavebank.wave.security;

import com.wavebank.wave.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserRegistrationDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //fetch user details by their email and convert User object into UserRegistrationDetails object
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserRegistrationDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
