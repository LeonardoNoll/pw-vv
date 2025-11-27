package dev.ifrs.data;

import java.util.ArrayList;
import java.util.List;

import dev.ifrs.model.Book;
import jakarta.inject.Singleton;

@Singleton
public class DataBase {

    private List<Book> books;

    public DataBase() {
        this.books = new ArrayList<>();

        // Initialize with some sample data
        Book book1 = new Book();
        book1.setTitle("1984");
        book1.setAuthor("George Orwell");
        book1.setIsbn("978-0451524935");

        Book book2 = new Book();
        book2.setTitle("To Kill a Mockingbird");
        book2.setAuthor("Harper Lee");
        book2.setIsbn("978-0061120084");

        books.add(book1);
        books.add(book2);
    }

    public List<Book> getBooks() {
        return books;
    }

    
}
