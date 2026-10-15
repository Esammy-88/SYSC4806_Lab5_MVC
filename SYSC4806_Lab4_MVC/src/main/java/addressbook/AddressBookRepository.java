package addressbook;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

public interface AddressBookRepository extends CrudRepository<AddressBook, Long> {

    // Loads the buddies in the same query. open-in-view is off, so the view
    // controller can't rely on lazy loading while the template renders.
    // Not exported as a REST endpoint; only the Thymeleaf controller uses it.
    @RestResource(exported = false)
    @Query("select a from AddressBook a left join fetch a.buddies where a.id = :id")
    Optional<AddressBook> findWithBuddiesById(@Param("id") Long id);
}
