package com.library;

import com.library.domain.Library;
import com.library.service.BorrowingService;

public class LibraryDemo {
    public static void main(String[] args) {
        Library library = new Library();
        BorrowingService borrowingService = new BorrowingService(library);

        library.addBook("1357924680135", "The Pragmatic Programmer", "Dave Thomas", 1999);
        library.addBook("1357924680135", "The Pragmatic Programmer", "Dave Thomas", 1999);
        library.addBook("2468013579246", "Clean Code", "Robert Martin", 2008);
        library.addBook("2468013579246", "Clean Code", "Robert Martin", 2008);

        System.out.println("Initial state:");
        System.out.println(library);

        String borrowId = "";

        try {
            borrowId = borrowingService.borrowBook("2468013579246");
            System.out.println("\nAfter borrowing Clean Code:");
            System.out.println(library);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            borrowingService.returnBook(borrowId);
            System.out.println("\nAfter returning Clean Code:");
            System.out.println(library);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            borrowingService.borrowBook("9999999999999");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
