package com.orangedragon1.server.utils;

import com.orangedragon1.server.models.auth.AuthenticationRequest;
import com.orangedragon1.server.models.auth.RegisterRequest;
import jakarta.json.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ResponseUtils {

    public JsonObject success(String message) {
        return Json.createObjectBuilder()
                .add("success", message)
                .add(
                        "timestamp",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                )
                .build();
    }

    public JsonObject validateTokenResponse(boolean isValid) {
        if (isValid) {
            return Json.createObjectBuilder()
                    .add("status", "success")
                    .add("message", "Token is valid")
                    .add("valid", true)
                    .add(
                            "timestamp",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                    )
                    .build();
        } else {
            return Json.createObjectBuilder()
                    .add("status", "error")
                    .add("message", "Token is invalid")
                    .add("valid", false)
                    .add(
                            "timestamp",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                    )
                    .build();
        }
    }

    public JsonObject registrationResponse(boolean isRegistered) {
        if (isRegistered) {
            return Json.createObjectBuilder()
                    .add("status", "success")
                    .add("message", "Registration successful")
                    .add(
                            "timestamp",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                    )
                    .build();
        } else {
            return Json.createObjectBuilder()
                    .add("status", "error")
                    .add("message", "Registration failed")
                    .add("error", "Username or email is already in use")
                    .add(
                            "timestamp",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                    )
                    .build();
        }
    }

    public JsonObject registrationError(RegisterRequest registerReq) {

        JsonObjectBuilder jObjBuilder = Json.createObjectBuilder();
        jObjBuilder
                .add("status", "error")
                .add("message", "Registration failed");

        List<String> emptyFieldList = RequestUtils.listOfNull(registerReq);
        JsonArrayBuilder jArrBuilder = Json.createArrayBuilder();

        for (String field : emptyFieldList) {
            jArrBuilder.add(Json.createObjectBuilder()
                    .add("field", field)
                    .add("message", StringUtils.capitalize("%s must not be empty".formatted(field)))
                    .build());
        }

        jObjBuilder
                .add("errors", jArrBuilder.build())
                .add(
                        "timestamp",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                );

        return jObjBuilder.build();
    }

    public JsonObject authenticationResponse(String jwtToken) {
        if (jwtToken == "Bad credentials") {
            return Json.createObjectBuilder()
                    .add("status", "error")
                    .add("message", "Authentication failed")
                    .add("error", jwtToken)
                    .add(
                            "timestamp",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                    )
                    .build();
        } else {
            return Json.createObjectBuilder()
                    .add("status", "success")
                    .add("message", "Authentication successful")
                    .add("token", jwtToken)
                    .add(
                            "timestamp",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                    )
                    .build();
        }
    }

    public JsonObject authenticationError(AuthenticationRequest authReq) {

        JsonObjectBuilder jObjBuilder = Json.createObjectBuilder();
        jObjBuilder
                .add("status", "error")
                .add("message", "Authentication failed");

        List<String> emptyFieldList = RequestUtils.listOfNull(authReq);
        JsonArrayBuilder jArrBuilder = Json.createArrayBuilder();

        for (String field : emptyFieldList) {
            if (field.equals("username") && !emptyFieldList.contains("email")) {
                continue;
            } else if (field.equals("email") && !emptyFieldList.contains("username")) {
                continue;
            }

            jArrBuilder.add(Json.createObjectBuilder()
                    .add("field", field)
                    .add("message", StringUtils.capitalize("%s must not be empty".formatted(field)))
                    .build());
        }

        jObjBuilder
                .add("errors", jArrBuilder.build())
                .add(
                        "timestamp",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                );
        return jObjBuilder.build();
    }
}
