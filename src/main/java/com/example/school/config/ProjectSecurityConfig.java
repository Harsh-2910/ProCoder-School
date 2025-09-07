package com.example.school.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // permit all requests
        http
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers("/saveMsg");
                    csrf.ignoringRequestMatchers("/public/**");
                    csrf.ignoringRequestMatchers("/api/**");
                    csrf.ignoringRequestMatchers("/data-api/**");
                    csrf.ignoringRequestMatchers("/proCoder/actuator/**");
                })
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/dashboard").authenticated()
                            .requestMatchers("/displayMessages/**").hasRole("ADMIN")
                            .requestMatchers("/closeMsg/**").hasRole("ADMIN")
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/proCoder/actuator/**").hasRole("ADMIN");
                    requests.requestMatchers("/api/**").authenticated();
                    requests.requestMatchers("/data-api/**").authenticated();
                    requests.requestMatchers("/displayProfile").authenticated();
                    requests.requestMatchers("/updateProfile").authenticated();
                    requests.requestMatchers("/student/**").hasRole("STUDENT");
                    requests.requestMatchers("/", "/home").permitAll();
                    requests.requestMatchers("/holidays/**").permitAll();
//                            requests.requestMatchers("/data-api/**").permitAll();
//                            requests.requestMatchers("/profile/**").permitAll();
//                            requests.requestMatchers("/courseses/**").permitAll();
//                            requests.requestMatchers("/contacts/**").permitAll();
                    requests.requestMatchers("/courses").permitAll();
                    requests.requestMatchers("/contact").permitAll();
                    requests.requestMatchers("/saveMsg").permitAll();
                    requests.requestMatchers("/about").permitAll();
                    requests.requestMatchers("/assets/**").permitAll();
                    requests.requestMatchers("/public/**").permitAll();
                    requests.requestMatchers("/login").permitAll();
                    requests.requestMatchers("/logout").permitAll();
        })
        .formLogin(login->{
            login
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard")
                    .failureUrl("/login?error=true")
                    .permitAll();
        })
//        .logout(logout->{
//            logout
//                    .logoutUrl("/logout")
//                    .logoutSuccessUrl("/login?logout=true")
//                    .invalidateHttpSession(true)
//                    .permitAll();
//        })
        .httpBasic(Customizer.withDefaults());
        return (SecurityFilterChain)http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
