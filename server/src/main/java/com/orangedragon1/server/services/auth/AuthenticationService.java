package com.orangedragon1.server.services.auth;

import com.orangedragon1.server.models.auth.*;
import com.orangedragon1.server.repositories.auth.UserRepository;
import com.orangedragon1.server.utils.auth.AuthenticationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepo;
    private final PasswordEncoder pwEncoder;
    private final JwtService jwtSvc;
    private final UserDetailsService userDetailsSvc;
    private final AuthenticationManager authManager;

    public boolean register(RegisterRequest registerReq) {
        /*
        register the user and return true if successful
         */

        Optional<User> optUsername = userRepo.findByUsername(registerReq.getUsername());
        Optional<User> optEmail = userRepo.findByEmail(registerReq.getEmail());
        if (optUsername.isPresent() || optEmail.isPresent()) {
            return false;
        }

        try {
            var user = User.builder()
                    .firstName(registerReq.getFirstName())
                    .lastName(registerReq.getLastName())
                    .username(registerReq.getUsername())
                    .email(registerReq.getEmail())
                    .password(pwEncoder.encode(registerReq.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepo.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String authenticate(AuthenticationRequest authReq) {
        /*
        authenticate the user and return the jwt token
         */
        if (null == authReq.getUsername() && null != authReq.getEmail()) {
            // check if email is present and username is not
            Optional<User> optEmail = userRepo.findByEmail(authReq.getEmail());
            // check if there is a user with the given email
            if (optEmail.isPresent()) {
                User user = optEmail.get();
                authReq.setUsername(user.getUsername());

                try {
                    authManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    authReq.getUsername(),
                                    authReq.getPassword()
                            )
                    );
                    return jwtSvc.generateToken(user);
                } catch (Exception e) {
//                    e.printStackTrace();
                    return "Bad credentials";
                }

            } else {
                // if there is no user with the given email
                return "Bad credentials";
            }
        } else {
            try {
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authReq.getUsername(),
                                authReq.getPassword()
                        )
                );
                Optional<User> optUsername = userRepo.findByUsername(authReq.getUsername());

                if (optUsername.isEmpty()) {
                    throw new UsernameNotFoundException("User not found");
                }

                User user = optUsername.get();
                return jwtSvc.generateToken(user);
            } catch (Exception e) {
//                e.printStackTrace();
                return "Bad credentials";
            }
        }
    }

    public boolean isManager() {
        /*
        check if the user is a manager
         */
        System.out.println(getAuthorities().toString());
        return getAuthorities().contains("MANAGER");
    }

    public List<String> getAuthorities() {
        /*
        get the authorities of the user
         */
        return jwtSvc.extractAuthorities(AuthenticationServiceUtils.getCurrentToken());
    }

    public boolean isTokenValid(String jwtToken) {
        String username = jwtSvc.extractUsername(jwtToken);
        if (null == username) {
            return false;
        }
        UserDetails userDetails = this.userDetailsSvc.loadUserByUsername(username);
        if (null == userDetails) {
            return false;
        }
        return jwtSvc.isTokenValid(jwtToken, userDetails);
    }
}
