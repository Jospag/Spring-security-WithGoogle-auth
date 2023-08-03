package org.example.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private String firstName;
    private String lastName;
    private String message;
}
