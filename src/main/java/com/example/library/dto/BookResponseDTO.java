package com.example.library.dto;

public record BookResponseDTO(
    Long id,
    String title,
    String isbn,
    Long authorId
) {}
