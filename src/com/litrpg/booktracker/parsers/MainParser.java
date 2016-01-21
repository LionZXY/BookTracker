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
    public static List<IBook> books = new ArrayList<>();
    public static List<Author> authors = new ArrayList<>();

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
        if (!books.contains(book)) {
            books.add(book);
            BookTracker.DB.addBookInTable(book);
            book.setDBid(BookTracker.DB.getIdBook(book));
            for (Author author : book.getAuthors()) {
                author.addBook(book);
            }
        }

    }

    public static void addAuthor(Author author) {
        if (!authors.contains(author)) {
            authors.add(author);
            BookTracker.DB.addAuthorInTable(author);
            author.setIdDB(BookTracker.DB.getIdAuthor(author));
        }
    }

    public static List<Author> getAllAuthorById(List<Integer> arr) {
        List<Author> authorsL = new ArrayList<>();
        for (Author author : authors)
            if (arr.contains(author.getInDB()))
                authorsL.add(author);
        return authorsL;
    }
}
