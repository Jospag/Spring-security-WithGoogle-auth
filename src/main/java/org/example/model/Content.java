package org.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.example.model.Enum.Type;
import org.example.model.Enum.Status;

import java.time.LocalDateTime;

public record Content (
        Integer id,
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotBlank
        Status status,
        @NotBlank
        Type contentType,
        @NotEmpty
        LocalDateTime dateCreated,
        @NotEmpty  
        LocalDateTime dateUpdated,
        String url

){
}
