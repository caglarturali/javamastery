package blog.javamastery.library.demo;

import blog.javamastery.library.domain.BookInfo;
import blog.javamastery.library.domain.BorrowingRecord;
import blog.javamastery.library.domain.Library;
import blog.javamastery.library.service.BorrowingService;

public class LibraryDemo {
    public static void main(String[] args) {
        Library library = new Library();
        BorrowingService borrowingService = new BorrowingService(library);

        var pragProg = new BookInfo("1357924680135", "The Pragmatic Programmer", "Dave Thomas", 1999);
        var cleanCode = new BookInfo("2468013579246", "Clean Code", "Robert Martin", 2008);

        library.addBook(pragProg);
        library.addBook(pragProg);
        library.addBook(cleanCode);
        library.addBook(cleanCode);

        System.out.println("Initial state:");
        System.out.println(library);

        BorrowingRecord borrowId;
        BorrowingRecord returnId;

        try {
            borrowId = borrowingService.borrowBook("2468013579246", "John Doe");
            System.out.println("\nAfter borrowing Clean Code:");
            System.out.println(borrowId);
            System.out.println(library);

            returnId = borrowingService.returnBook(borrowId.copyId());
            System.out.println("\nAfter returning Clean Code:");
            System.out.println(returnId);
            System.out.println(library);

            borrowingService.borrowBook("9999999999999", "John Doe");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static String getMemoryInfo() {
        Runtime rt = Runtime.getRuntime();
        long total = rt.totalMemory();
        long free = rt.freeMemory();
        long used = total - free;
        return String.format("Used memory: %,d bytes", used);
    }
}
