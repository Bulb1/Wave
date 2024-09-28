package com.wavebank.wave.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

//https://medium.com/@arijit83work/securityfilterchain-with-spring-security-in-spring-boot-4d8e244cef4a
//https://www.baeldung.com/spring-security-5-oauth2-login
//since Security 6: cors() is depracated and marked for removal
//https://stackoverflow.com/questions/77266685/spring-security-6-cors-is-deprecated-and-marked-for-removal

//https://www.linkedin.com/pulse/database-integration-postman-xmysql-umme-habiba
public class UserRegistrationSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/register/**").permitAll();
                    request.requestMatchers("/users/**")
                            .hasAnyAuthority("USER", "ADMIN");
                }).formLogin(Customizer.withDefaults()).build();

    }
}
