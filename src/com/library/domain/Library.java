package com.library.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Library implements BookRepository {
    private final Map<String, List<BookCopy>> copiesByIsbn;

    public Library() {
        this.copiesByIsbn = new HashMap<>();
    }

    @Override
    public void addBook(String isbn, String title, String author, int yearPublished) {
        var bookInfo = new BookInfo(isbn, title, author, yearPublished);
        addBook(bookInfo);
    }

    @Override
    public void addBook(BookInfo bookInfo) {
        var bookCopy = new BookCopy(bookInfo);
        copiesByIsbn.computeIfAbsent(bookInfo.isbn(), k -> new ArrayList<>())
                .add(bookCopy);
    }

    @Override
    public List<BookCopy> findAllByISBN(String isbn) {
        return copiesByIsbn.getOrDefault(isbn, List.of());
    }

    @Override
    public List<BookCopy> findAllByTitle(String title) {
        return copiesByIsbn.values()
                .stream()
                .flatMap(List::stream)
                .filter(book -> book.getBookInfo().title().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }

    @Override
    public Optional<BookCopy> findOneById(String copyId) {
        try {
            UUID searchId = UUID.fromString(copyId);
            return copiesByIsbn.values()
                    .stream()
                    .flatMap(List::stream)
                    .filter(book -> book.getCopyIdAsUUID().equals(searchId))
                    .findFirst();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        if (copiesByIsbn.isEmpty()) {
            return "Library is empty";
        }

        return "Library Contents:\n" + copiesByIsbn.values()
                .stream()
                .flatMap(List::stream)
                .map("- %s"::formatted)
                .collect(Collectors.joining("\n"));

    }
}
