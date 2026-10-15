# SYSC 4806 Lab 4: Spring MVC AddressBook

Builds on the Lab 3 Part I project (Spring Data JPA + in-memory H2).

- **REST (JSON):** Spring Data REST exposes the two repositories under `/api`
  (`/api/addressBooks`, `/api/buddyInfos`). A buddy is added to an address book
  by POSTing its URI (`text/uri-list`) to `/api/addressBooks/{id}/buddies`.
- **View:** `AddressBookViewController` serves `GET /addressbook/{id}`, rendered
  with the Thymeleaf template `templates/addressbook.html`. It lists the buddies
  of the address book and returns 404 for an unknown id.
- **Requests:** see `requests.txt` (curl) or `requests.http` (IntelliJ HTTP client).
- **Tests:** `mvn test` runs a context-load test and two MockMvc tests for the view.

Run with `mvn spring-boot:run` (Java 17+), then follow `requests.txt`.
