package addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressBookMockTest {

    @Autowired MockMvc mvc;
    @Autowired AddressBookRepository repository;

    @Test
    void viewShowsBuddy() throws Exception {
        AddressBook book = new AddressBook();
        book.addBuddy(new BuddyInfo("Alice", "613-555-0101"));
        book = repository.save(book);

        mvc.perform(get("/addressbook/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("addressbook"))
                .andExpect(content().string(containsString("Alice")));
    }

    @Test
    void unknownIdReturns404() throws Exception {
        mvc.perform(get("/addressbook/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void postCreatesAddressBook() throws Exception {
        mvc.perform(post("/api/addressBooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}