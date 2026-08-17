package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookRequestDTO(
    @NotBlank(message = "Title is required")
    String title,
    
    String isbn,
    
    @NotNull(message = "Author ID is required")
    Long authorId
) {}
