package com.example.library.service;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public BookResponseDTO createBook(BookRequestDTO request) {
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + request.authorId()));

        Book book = new Book();
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setAuthor(author);

        Book saved = bookRepository.save(book);
        return mapToResponse(saved);
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToResponse(book);
    }

    @Transactional
    public BookResponseDTO updateBook(Long id, BookRequestDTO request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + request.authorId()));

        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setAuthor(author);

        Book updated = bookRepository.save(book);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    private BookResponseDTO mapToResponse(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAuthor() != null ? book.getAuthor().getId() : null
        );
    }
}
