package com.example.library.service;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getBookById_ShouldThrowResourceNotFoundException_WhenBookDoesNotExist() {
        Long bookId = 99L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(bookId));
    }
}
