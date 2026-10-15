package addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddressBookApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void viewListsTheBuddiesOfAnAddressBook() throws Exception {
        AddressBook book = new AddressBook();
        book.addBuddy(new BuddyInfo("Alex", "613-555-0101"));
        book.addBuddy(new BuddyInfo("Jordan", "613-555-0102"));
        book = addressBookRepository.save(book);

        mockMvc.perform(get("/addressbook/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Alex")))
                .andExpect(content().string(containsString("613-555-0102")));
    }

    @Test
    void viewReturns404ForUnknownAddressBook() throws Exception {
        mockMvc.perform(get("/addressbook/999999"))
                .andExpect(status().isNotFound());
    }
}
