package addressbook;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AddressBookViewController {

    private final AddressBookRepository addressBookRepository;

    public AddressBookViewController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<AddressBook> books = new ArrayList<>();
        addressBookRepository.findAll().forEach(books::add);
        model.addAttribute("addressBooks", books);
        return "index";
    }

    @GetMapping("/addressbook/{id}")
    public String viewAddressBook(@PathVariable Long id, Model model) {
        AddressBook addressBook = addressBookRepository.findWithBuddiesById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No address book with id " + id));
        model.addAttribute("addressBook", addressBook);
        return "addressbook";
    }
}
