package by.vtu.vtuproject.controller;

import by.vtu.vtuproject.entity.Bookmark;
import by.vtu.vtuproject.exception.UserNotFoundException;
import by.vtu.vtuproject.repository.AccountRepository;
import by.vtu.vtuproject.repository.BookmarkRepository;
import by.vtu.vtuproject.resource.BookmarkResource;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkRestController {
    private final BookmarkRepository bookmarkRepository;
    private final AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(Principal principal, @RequestBody Bookmark input) {
        String userName = principal.getName();
        this.validateUser(userName);
        return accountRepository
                .findAccountByUserName(userName)
                .map(account -> {
                    Bookmark result = bookmarkRepository.save(new Bookmark(account, input.getUri(), input.getDescription()));
                    Link forOneBookMark = new BookmarkResource(result).getLink("self").orElseThrow();

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setLocation(URI.create(forOneBookMark.getHref()));
                    return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
                }).orElseThrow();
    }

    @RequestMapping(value = "/{bookmarkId}", method = RequestMethod.GET)
    public Bookmark readBookmark(Principal principal, @PathVariable Long bookmarkId) {
        String userName = principal.getName();
        this.validateUser(userName);
        return bookmarkRepository.findById(bookmarkId).orElseThrow();
    }

    @RequestMapping(method = RequestMethod.GET)
    public CollectionModel<BookmarkResource> readBookmarks(Principal principal) {
        String userName = principal.getName();
        this.validateUser(userName);
        List<BookmarkResource> bookmarkResourceList = bookmarkRepository.findByAccountUserName(userName)
                .stream()
                .map(BookmarkResource::new)
                .toList();
        return CollectionModel.of(bookmarkResourceList);
    }

    private void validateUser(String userName) {
        accountRepository.findAccountByUserName(userName).orElseThrow(
                () -> new UserNotFoundException(userName));
    }
}
