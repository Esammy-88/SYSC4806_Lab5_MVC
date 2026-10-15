package addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressBookHttpTest {

    @Autowired TestRestTemplate rest;
    @Autowired AddressBookRepository repository;

    @Test
    void viewOverHttp() {
        AddressBook book = new AddressBook();
        book.addBuddy(new BuddyInfo("Bob", "613-555-0202"));
        book = repository.save(book);

        ResponseEntity<String> response =
                rest.getForEntity("/addressbook/{id}", String.class, book.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Bob");
    }

    @Test
    void postThenGetOverHttp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> created =
                rest.postForEntity("/api/addressBooks", new HttpEntity<>("{}", headers), String.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location = created.getHeaders().getLocation();
        ResponseEntity<String> fetched = rest.getForEntity(location, String.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}