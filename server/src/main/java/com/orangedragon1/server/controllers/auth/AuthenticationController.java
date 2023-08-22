package com.orangedragon1.server.controllers.auth;

import com.orangedragon1.server.models.auth.AuthenticationRequest;
import com.orangedragon1.server.models.auth.RegisterRequest;
import com.orangedragon1.server.repositories.auth.UserRepository;
import com.orangedragon1.server.services.auth.AuthenticationService;
import com.orangedragon1.server.utils.ResponseUtils;
import jakarta.json.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authSvc;
    private final ResponseUtils respUtils;
    private final UserRepository userRepo;

    @GetMapping(path = "/demo-controller")
    public ResponseEntity<String> getDemo() {
        System.out.println("auth/demo-controller is called");
        return ResponseEntity.ok("Greetings from unsecured endpoint.");
    }

    @PostMapping(path = "/validate-token")
    public ResponseEntity<String> postValidateToken(@RequestBody String jwtToken) {
        boolean isValid = authSvc.isTokenValid(jwtToken);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(respUtils.validateTokenResponse(isValid).toString());
    }

    @PostMapping(path = "/register")
    public ResponseEntity<String> postRegister(@RequestBody RegisterRequest registerReq) {
        if (!registerReq.isValid()) {
            JsonObject errorObj = respUtils.registrationError(registerReq);
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(errorObj.toString());
        }

        boolean isRegistered = authSvc.register(registerReq);
        int httpStatusCode = 200;

        if (!isRegistered) {
            httpStatusCode = 409;
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(httpStatusCode))
                .body(respUtils.registrationResponse(isRegistered).toString());
    }

    @PostMapping(path = "/authenticate")
    public ResponseEntity<String> postAuthenticate(@RequestBody AuthenticationRequest authReq) {
        if (!authReq.isValid()) {
            JsonObject errorObj = respUtils.authenticationError(authReq);
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(errorObj.toString());
        }

        String jwtToken = authSvc.authenticate(authReq);
        int httpStatusCode = 200;

        if (jwtToken == "Bad credentials") {
            httpStatusCode = 401;
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(httpStatusCode))
                .body(respUtils.authenticationResponse(jwtToken).toString());
    }

}
