package blog.javamastery.library.repository;

import blog.javamastery.library.domain.BookCopy;
import blog.javamastery.library.domain.BookInfo;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void addBook(String isbn, String title, String author, int yearPublished);

    void addBook(BookInfo bookInfo);

    List<BookCopy> findAllByISBN(String isbn);

    List<BookCopy> findAllByTitle(String title);

    Optional<BookCopy> findOneById(String copyId);
}
