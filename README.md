# SYSC 4806 Lab 5 – AddressBook with CI/CD

![CI](https://github.com/Esammy-88/SYSC4806_Lab5_MVC/actions/workflows/maven.yml/badge.svg)

**Name:** Sammy Eyongorock

**Azure deployment:** https://sammy-addressbook-4806.azurewebsites.net

## About

Spring Boot AddressBook web app (Thymeleaf views + Spring Data REST API under `/api`), with:

- Integration tests using MockMvc and TestRestTemplate
- Continuous integration via GitHub Actions (build + tests on every push to `master`)
- Continuous deployment to Microsoft Azure App Service
- New feature: `BuddyInfo` has an `address` field, stored, exposed through the REST API and shown on the address book page (tests in `BuddyAddressTest`)
- Home page at `/` linking to every address book and the REST API

> Free F1 tier: the first request after a quiet period can take up to a minute while the app wakes up.

## Endpoints

| Path | What it does |
| --- | --- |
| `GET /` | Home page listing all address books |
| `GET /addressbook/{id}` | Thymeleaf view of one address book (404 if unknown) |
| `/api/addressBooks`, `/api/buddyInfos` | Spring Data REST (JSON) |

Sample requests: `SYSC4806_Lab4_MVC/requests.txt` (curl) and `requests.http` (IntelliJ).
