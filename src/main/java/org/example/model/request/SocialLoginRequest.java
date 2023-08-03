package org.example.model.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
//@NoArgsConstructor
public class SocialLoginRequest {
    private String id_token;
    private String sub;
    @Email
    private String email;
    private boolean email_verified;
    private String name;
    private String picture;
    private String given_name;
    private String family_name;
}
