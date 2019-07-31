package com.example.demo.integration;

import com.example.demo.Entities.Book;
import com.example.demo.Entities.Reader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@WithMockUser(username = "admin", password = "admin", roles = {"USER","ADMIN"})
public class IntegrationReaderController {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    private List<Reader> readersDummies;

    private Reader dummyToPost;

    private Book dummyRentedBook;

    @Before
    public void setup() {

        readersDummies = new ArrayList<>();
        readersDummies.add(new Reader( "Adam", "Niezgodka", new BigDecimal("0.0")));
        Reader reader = new Reader( "Piotr", "Wielki", new BigDecimal("0.0"));
        readersDummies.add(reader);
        dummyToPost = new Reader("Kaczor","Donald",new BigDecimal("0.0"));

        LocalDate date= LocalDate.parse("2019-05-30");
        Book book = new Book("Dziady", "Mickiewicz");
        book.setReturnDate(date);
        book.setReader(reader);
        dummyRentedBook = book;
    }

    @Test
    public void testGetReaderByIdWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/readers/{id}", 2)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(readersDummies.get(1).getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(readersDummies.get(1).getLastName()));
    }

    @Test
    public void testGetReaderByIdBadIdWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/readers/{id}", 22)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetReaderAllWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/readers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value(readersDummies.get(0).getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName").value(readersDummies.get(0).getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].firstName").value(readersDummies.get(1).getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].lastName").value(readersDummies.get(1).getLastName()));
    }

    @Test
    public void testCreateReaderWithMockMVC() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .post("/readers")
                .content(om.writeValueAsString(dummyToPost))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(dummyToPost.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(dummyToPost.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.punishment").value(dummyToPost.getPunishment()));
    }

    @Test
    public void testRentingBookWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/readers/{readerId}/books/{bookId}", 2,1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(dummyRentedBook.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(dummyRentedBook.getAuthor()));

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/readers/{id}", 2)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(readersDummies.get(1).getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(readersDummies.get(1).getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booksDTO[0].title").value(dummyRentedBook.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booksDTO[0].author").value(dummyRentedBook.getAuthor()));
    }

    @Test
    public void testRentingBookBadIdWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/readers/{readerId}/books/{bookId}", 2,13)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRentingBookAlreadyRentedWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/readers/{readerId}/books/{bookId}", 2,3)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteReaderWithMockMVC() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete("/readers/{id}", 3) )
                .andExpect(status().isNoContent());
    }
}