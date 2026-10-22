package addressbook;

import org.junit.jupiter.api.Assertions;
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

    // ---- Lab 5 ----

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

    @Test
    void viewShowsBuddyAddress() throws Exception {
        AddressBook book = new AddressBook();
        book.addBuddy(new BuddyInfo("Dana", "613-555-0404", "1125 Colonel By Dr"));
        book = repository.save(book);

        mvc.perform(get("/addressbook/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1125 Colonel By Dr")));
    }

    @Test
    void postBuddyWithAddressThroughApi() throws Exception {
        String location = mvc.perform(post("/api/buddyInfos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Carol\",\"phoneNumber\":\"613-555-0303\",\"address\":\"75 Laurier Ave\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        Assertions.assertNotNull(location);
        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("75 Laurier Ave"));
    }

    // ---- Lab 6 ----

    @Test
    void rootListsAddressBooks() throws Exception {
        AddressBook book = repository.save(new AddressBook());

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("/addressbook/" + book.getId())));
    }

    @Test
    void createAddressBookRedirectsToRoot() throws Exception {
        mvc.perform(post("/addressbook"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void addBuddyFormSavesAndRedirects() throws Exception {
        AddressBook book = repository.save(new AddressBook());

        mvc.perform(post("/addressbook/{id}/buddies", book.getId())
                        .param("name", "Eve")
                        .param("phoneNumber", "613-555-0505")
                        .param("address", "1 Rideau St"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/addressbook/" + book.getId()));

        mvc.perform(get("/addressbook/{id}", book.getId()))
                .andExpect(content().string(containsString("Eve")))
                .andExpect(content().string(containsString("1 Rideau St")));
    }
}