package org.example.model.response;

import lombok.Builder;
import lombok.Data;

import java.security.PrivateKey;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String refreshToken;
    private String userId;

}
