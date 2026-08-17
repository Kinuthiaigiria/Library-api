package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequestDTO(
    @NotBlank(message = "Name is required")
    String name
) {}
