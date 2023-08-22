//package com.orangedragon1.server.components;
//
//import com.orangedragon1.server.models.auth.Role;
//import com.orangedragon1.server.models.auth.User;
//import com.orangedragon1.server.repositories.auth.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DataInitializer implements CommandLineRunner {
//
//    private final UserRepository userRepo;
//    private final PasswordEncoder pwEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // default users
//        User manager = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .username("johndoe")
//                .email("johndoe@example.com")
//                .password(pwEncoder.encode("password"))
//                .role(Role.MANAGER)
//                .build();
//        userRepo.save(manager);
//
//        User user = User.builder()
//                .firstName("Jack")
//                .lastName("Doe")
//                .username("jackdoe")
//                .email("jackdoe@example.com")
//                .password(pwEncoder.encode("password"))
//                .role(Role.USER)
//                .build();
//        userRepo.save(user);
//    }
//}
