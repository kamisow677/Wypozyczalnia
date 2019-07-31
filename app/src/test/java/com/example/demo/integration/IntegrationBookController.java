package com.example.demo.integration;

import com.example.demo.Entities.Book;
import com.example.demo.Entities.Reader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
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
public class IntegrationBookController {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    private List<Book> booksDummies;

    private Book dummyToPost;

    @Before
    public void setup() {
        booksDummies = new ArrayList<>();
        Reader reader1 = new Reader( "Piotr", "Wielki", new BigDecimal("0.0"));
        Reader reader2 =  new Reader( "Adam", "Niezgodka", new BigDecimal("0.0"));

        //BOOK ID = 1
        booksDummies.add(new Book( "Dziady", "Mickiewicz"));
        //BOOK ID = 2
        booksDummies.add(new Book("Potop", "Sienkiewicz"));

        //BOOK ID = 3
        LocalDate date1 = LocalDate.parse("2019-05-30");
        Book book1 = new Book("Cyberiada", "Lem");
        book1.setReturnDate(date1);
        book1.setReader(reader1);
        booksDummies.add(book1);

        //BOOK ID = 4
        LocalDate date2 = LocalDate.parse("2019-05-30");
        Book book2 = new Book("Dżuma", "Cumus");
        book2.setReturnDate(date2);
        book2.setReader(reader2);
        booksDummies.add(book2);

        dummyToPost = new Book( "Biesy", "Dostojewski");
    }

    @Test
    public void testGetBookByIdWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/books/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(booksDummies.get(0).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(booksDummies.get(0).getAuthor()));
    }

    @Test
    public void testGetBookByIdBadIdWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/books/{id}", 15)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBookAllWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/books")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].title").value((booksDummies.get(1).getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].author").value(booksDummies.get(1).getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].title").value((booksDummies.get(2).getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].author").value(booksDummies.get(2).getAuthor()));
    }

    @Test
    public void testCreateBookWithMockMVC() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/books")
                .content(om.writeValueAsString(dummyToPost))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
               // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(dummyToPost.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(dummyToPost.getTitle()));
    }

    @Test
    public void testGivebackBookAllWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/books/returnBook/{id}", 4)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnDate").value(IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reader").doesNotExist());
    }

    @Test
    public void testGivebackBookBadIdAllWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/books/returnBook/{id}", 14)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGivebackNotRentedAllWithMockMVC() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/books/returnBook/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteBookWithMockMVC() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete("/books/{id}", 3) )
                .andExpect(status().isNoContent());
    }

}
