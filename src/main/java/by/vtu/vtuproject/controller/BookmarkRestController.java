package by.vtu.vtuproject.controller;

import by.vtu.vtuproject.entity.Bookmark;
import by.vtu.vtuproject.exception.UserNotFoundException;
import by.vtu.vtuproject.repository.AccountRepository;
import by.vtu.vtuproject.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;

@RestController
@RequestMapping("/{userName}/bookmarks")
@RequiredArgsConstructor
public class BookmarkRestController {
    private final BookmarkRepository bookmarkRepository;
    private final AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable String userName, @RequestBody Bookmark input) {
        this.validateUser(userName);
        return accountRepository
                .findAccountByUserName(userName)
                .map(account -> {
                    Bookmark result = bookmarkRepository.save(new Bookmark(account, input.getUri(), input.getDescription()));
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setLocation(ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri());
                    return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
                }).orElseThrow();
    }

    @RequestMapping(value = "/{bookmarkId}", method = RequestMethod.GET)
    Bookmark readBookmark(@PathVariable String userName, @PathVariable Long bookmarkId) {
        this.validateUser(userName);
        return bookmarkRepository.findById(bookmarkId).orElseThrow();
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Bookmark> readBookmarks(@PathVariable String userName) {
        this.validateUser(userName);
        return bookmarkRepository.findByAccountUserName(userName);
    }

    private void validateUser(String userName) {
        accountRepository.findAccountByUserName(userName).orElseThrow(
                () -> new UserNotFoundException(userName));
    }
}
