package com.orangedragon1.server.models.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String email;
    private String password;


    public boolean isValid() {
        /*
        checks if request is valid
         */
        return (username != null || email != null) &&
                password != null;
    }
}
