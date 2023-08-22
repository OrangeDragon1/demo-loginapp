package com.orangedragon1.server.models.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;

    public boolean isValid() {
        /*
        checks if request is valid
         */
        return firstName != null &&
                lastName != null &&
                username != null &&
                email != null &&
                password != null;
    }
}
