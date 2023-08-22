package com.orangedragon1.server.controllers.demo;

import com.orangedragon1.server.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/demo")
public class DemoController {

    private final AuthenticationService authSvc;

    @GetMapping(path = "/demo-controller")
    public ResponseEntity<String> getDemo() {

        return ResponseEntity.ok("Greetings from secured endpoint.");
    }

    @GetMapping(path = "/demo-controller/manager")
    public ResponseEntity<String> getDemoManager() {
        boolean isManager = authSvc.isManager();

        if (isManager) {
            return ResponseEntity.ok("Greetings from secured manager endpoint.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this endpoint.");
        }
    }
}
