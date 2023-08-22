package com.orangedragon1.server.controllers.demo;

import com.orangedragon1.server.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authSvc;

    @GetMapping(path = "/user")
    public ResponseEntity<String> getUser() {
        System.out.println("auth/demo-controller is called");


        return ResponseEntity.ok("Greetings from unsecured endpoint.");
    }
}
