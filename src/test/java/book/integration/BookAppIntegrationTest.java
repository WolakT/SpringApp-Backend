package book.integration;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;

import book.dto.BookResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import book.dao.BookRepository;
import book.dto.Book;
import book.entity.BookEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookAppIntegrationTest {

    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    public void shouldFindAllBooks() throws Exception {

        insertBooks();
        List<Book> expectedBooks = getDefaultBooks();
        String expectedBooksAsString = objectMapper.writeValueAsString(expectedBooks);

        mockMvc.perform(MockMvcRequestBuilders.get("/book/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expectedBooksAsString));
    }

    @Test
    public void shouldAddBook() throws Exception {
        Book book = createBook("Pan Tadeusz", "Adam Mickiewicz", "Poemat", "978-1-4637-225-00");

        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldDeleteBookWhenExist() throws Exception {
        bookRepository.deleteAll();
        BookEntity book = createBookEntity("Pan Tadeusz", "Adam Mickiewicz", "Poemat", "978-1-4637-225-00");
        bookRepository.save(book);
        BookResponse bookResponse = new BookResponse("Successfully deleted");
        String bookResponseAsString = objectMapper.writeValueAsString(bookResponse);


        mockMvc.perform(MockMvcRequestBuilders.delete("/book/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(bookResponseAsString));
    }

    public void insertBooks() {
        BookEntity first = createBookEntity("Pan Tadeusz", "Adam Mickiewicz", "Poemat", "978-1-4637-225-00");
        BookEntity second = createBookEntity("Lalka", "Boleslaw Prus", "Powiesc", "978-83-7779-206-3");
        BookEntity third = createBookEntity("Maly Ksiaze", "Antoine de Saint-Exupéry", "Basn", "978-83-10-12843-0");
        bookRepository.save(asList(first, second, third));
    }

    private BookEntity createBookEntity(String title, String author, String category, String isbn) {
        BookEntity book = new BookEntity();
        book.setCategory(category);
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return book;
    }

    private List<Book> getDefaultBooks() {
        Book first = createBook("Pan Tadeusz", "Adam Mickiewicz", "Poemat", "978-1-4637-225-00");
        Book second = createBook("Lalka", "Boleslaw Prus", "Powiesc", "978-83-7779-206-3");
        Book third = createBook("Maly Ksiaze", "Antoine de Saint-Exupéry", "Basn", "978-83-10-12843-0");

        return asList(first, second, third);
    }

    private Book createBook(String title, String author, String category, String isbn) {
        Book book = new Book();
        book.setCategory(category);
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return book;
    }
}
