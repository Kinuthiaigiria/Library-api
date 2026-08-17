package com.example.library.service;

import com.example.library.dto.AuthorRequestDTO;
import com.example.library.dto.AuthorResponseDTO;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Author;
import com.example.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional
    public AuthorResponseDTO createAuthor(AuthorRequestDTO request) {
        Author author = new Author();
        author.setName(request.name());
        Author saved = authorRepository.save(author);
        return mapToResponse(saved);
    }

    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AuthorResponseDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return mapToResponse(author);
    }

    @Transactional
    public AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        author.setName(request.name());
        Author updated = authorRepository.save(author);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    private AuthorResponseDTO mapToResponse(Author author) {
        return new AuthorResponseDTO(author.getId(), author.getName());
    }
}
