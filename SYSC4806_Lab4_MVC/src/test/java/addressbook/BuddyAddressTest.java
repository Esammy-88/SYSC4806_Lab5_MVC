package addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the address field added to BuddyInfo in Lab 5.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BuddyAddressTest {

    @Autowired MockMvc mvc;
    @Autowired AddressBookRepository repository;

    @Test
    void buddyStoresAddress() {
        BuddyInfo buddy = new BuddyInfo("Sam", "613-555-0303", "1125 Colonel By Dr");
        assertThat(buddy.getAddress()).isEqualTo("1125 Colonel By Dr");

        buddy.setAddress("75 Laurier Ave E");
        assertThat(buddy.getAddress()).isEqualTo("75 Laurier Ave E");
    }

    @Test
    void viewShowsBuddyAddress() throws Exception {
        AddressBook book = new AddressBook();
        book.addBuddy(new BuddyInfo("Sam", "613-555-0303", "1125 Colonel By Dr"));
        book = repository.save(book);

        mvc.perform(get("/addressbook/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Address")))
                .andExpect(content().string(containsString("1125 Colonel By Dr")));
    }

    @Test
    void restPostAndGetKeepAddress() throws Exception {
        MvcResult created = mvc.perform(post("/api/buddyInfos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Riley\",\"phoneNumber\":\"613-555-0404\","
                                + "\"address\":\"1 Rideau St\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String location = created.getResponse().getHeader("Location");
        assertThat(location).isNotNull();

        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Riley"))
                .andExpect(jsonPath("$.address").value("1 Rideau St"));
    }

    @Test
    void homePageLinksToAddressBooks() throws Exception {
        AddressBook book = repository.save(new AddressBook());

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("/addressbook/" + book.getId())));
    }
}
