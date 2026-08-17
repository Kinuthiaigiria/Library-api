package com.example.library.controller;

import com.example.library.dto.BookRequestDTO;
import com.example.library.model.Author;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class BookControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private Author savedAuthor;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        Author author = new Author();
        author.setName("J.R.R. Tolkien");
        savedAuthor = authorRepository.save(author);
    }

    @Test
    void shouldCreateAndGetBook() throws Exception {
        BookRequestDTO requestDTO = new BookRequestDTO("The Hobbit", "1234567890", savedAuthor.getId());

        String response = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("The Hobbit")))
                .andExpect(jsonPath("$.isbn", is("1234567890")))
                .andExpect(jsonPath("$.authorId", is(savedAuthor.getId().intValue())))
                .andReturn().getResponse().getContentAsString();

        Long bookId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/v1/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookId.intValue())))
                .andExpect(jsonPath("$.title", is("The Hobbit")));
    }
}
