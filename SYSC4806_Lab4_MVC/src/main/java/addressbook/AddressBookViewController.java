package addressbook;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AddressBookViewController {

    private final AddressBookRepository addressBookRepository;

    public AddressBookViewController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    // Root page: table of all address books
    @GetMapping("/")
    public String listAddressBooks(Model model) {
        model.addAttribute("addressBooks", addressBookRepository.findAll());
        return "index";
    }

    // "Create" button on the root page
    @PostMapping("/addressbook")
    public String createAddressBook() {
        addressBookRepository.save(new AddressBook());
        return "redirect:/";
    }

    // One address book and its buddies
    @GetMapping("/addressbook/{id}")
    public String viewAddressBook(@PathVariable Long id, Model model) {
        model.addAttribute("addressBook", findOr404(id));
        return "addressbook";
    }

    // "Add buddy" form on the address book page
    @PostMapping("/addressbook/{id}/buddies")
    @Transactional
    public String addBuddy(@PathVariable Long id,
                           @RequestParam String name,
                           @RequestParam(required = false) String phoneNumber,
                           @RequestParam(required = false) String address) {
        AddressBook addressBook = findOr404(id);
        addressBook.addBuddy(new BuddyInfo(name, phoneNumber, address));
        // No save() needed: the book is managed inside this transaction, so the
        // new buddy is persisted on commit through CascadeType.PERSIST.
        return "redirect:/addressbook/" + id;
    }

    private AddressBook findOr404(Long id) {
        return addressBookRepository.findWithBuddiesById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No address book with id " + id));
    }
}