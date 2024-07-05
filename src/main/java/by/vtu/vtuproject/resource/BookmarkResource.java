package by.vtu.vtuproject.resource;

import by.vtu.vtuproject.controller.BookmarkRestController;
import by.vtu.vtuproject.entity.Bookmark;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class BookmarkResource extends RepresentationModel<BookmarkResource> {
    private final Bookmark bookmark;

    public BookmarkResource(Bookmark bookmark) {
        this.bookmark = bookmark;
        String userName = bookmark.getAccount().getUserName();
        this.add(Link.of(bookmark.getUri(), "bookmark-uri"));
        this.add(linkTo(BookmarkRestController.class, userName).withRel("bookmarks"));
        this.add(linkTo(methodOn(BookmarkRestController.class, userName).readBookmark(null, bookmark.getId())).withSelfRel());
    }

    public Bookmark getBookmark() {
        return bookmark;
    }
}
