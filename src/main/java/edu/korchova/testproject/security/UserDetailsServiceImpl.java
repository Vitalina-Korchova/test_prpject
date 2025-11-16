package edu.korchova.testproject.security;

/*
    @author Віталіна
    @project test_prpject
    @class UserDetailsServiceImpl
    @version 1.0.0
    @since 16.11.2025 - 16-30
*/
import edu.korchova.testproject.user.Role;
import edu.korchova.testproject.user.User;
import edu.korchova.testproject.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;

     @PostConstruct
  void init() {
      User user = User.builder()
              .firstName("Vitalina")
              .lastName("Korchova")
              .email("vit@gmail.com")
              .password(passwordEncoder.encode("12345"))
              .enabled(true)
              .accountLocked(false)
              .roles(List.of(Role.USER))
              .build();
     repository.save(user);
  }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return repository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}