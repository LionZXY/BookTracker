package com.litrpg.booktracker.parsers;

import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.mysql.MySql;

import java.util.ArrayList;
import java.util.List;

/**
 * com.litrpg.booktracker.parsers
 * Created by LionZXY on 18.01.2016.
 * BookTracker
 */
public abstract class MainParser {
    private static List<IBook> books = new ArrayList<>();
    private static List<Author> authors = new ArrayList<>();

    public static IBook getBook(String url) {
        IBook book = findBook(url);
        if (book == null)
            switch (TypeSite.getTypeFromUrl(url)) {
                case LITERA:
                    return new LitEraParser(url).parseBook();
            }
        return book;

    }

    public static IBook findBook(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        for (IBook book : books) {
            if (book.getUrl().equalsIgnoreCase(url))
                return book;
        }
        return null;
    }

    public static Author findAuthor(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        for (Author author : authors) {
            if (author.getUrl().equalsIgnoreCase(url))
                return author;
        }
        return null;
    }

    public static void addBook(IBook book) {
        books.add(book);
        BookTracker.DB.addBookInTable(book);
        book.setDBid(BookTracker.DB.getIdBook(book));
    }

    public static void addAuthor(Author author) {
        authors.add(author);
    }
}
