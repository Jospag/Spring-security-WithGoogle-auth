package org.example.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class SocialLogin {
    private String id;
    private String sub;
    private String email;
    private boolean verified_email;
    private boolean email_verified;
    private String name;
    private String picture;
    private String given_name;
    private String family_name;
    public SocialLogin(){}
}
