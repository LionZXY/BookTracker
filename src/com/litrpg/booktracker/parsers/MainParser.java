package com.litrpg.booktracker.parsers;

import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.helper.URLHelper;
import com.litrpg.booktracker.user.IUser;

import java.io.FileNotFoundException;
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
    public static List<IUser> users = new ArrayList<>();

    public static IBook getBook(String url) throws FileNotFoundException {
        IBook book = findBook(url);
        if (book == null)
            switch (TypeSite.getTypeFromUrl(url)) {
                case SAMLIB:
                    SamLibParser p = new SamLibParser(url);
                    return p.parseBook();
                case LITERA:
                    LitEraParser p2 = new LitEraParser(url);
                    return p2.parseBook();
            }
        return book;
    }

    public static Author getAuthor(String url) throws FileNotFoundException {
        Author author = findAuthor(url);
        if (URLHelper.isBook(url)) {
            Logger.getLogger().print("Попытка добавить произведение как автора " + url);
            return getBook(url).getAuthors().get(0);
        }
        if (author == null)
            switch (TypeSite.getTypeFromUrl(url)) {
                case SAMLIB:
                    return new SamLibParser(url).getAuthor();
                case LITERA:
                    return new LitEraParser(url).getAuthor();
            }
        return author;
    }

    public static IBook findBook(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        for (IBook book : books) {
            if (book.getUrl().startsWith(url))
                return book;
        }
        return null;
    }

    public static Author findAuthor(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        for (Author author : authors) {
            if (author.getUrl().startsWith(url))
                return author;
        }
        return null;
    }

    public static void addBook(IBook book) {
        if (!books.contains(book)) {
            books.add(book);
            BookTracker.DB.addBookInTable(book);
            for (Author author : book.getAuthors()) {
                author.addBook(book);
            }
        }

    }

    public static void addAuthor(Author author) {
        if (!authors.contains(author)) {
            authors.add(author);
            BookTracker.DB.addAuthorInTable(author);
        }
    }

    public static IUser addUser(IUser user) {
        if (!users.contains(user)) {
            users.add(user);
            BookTracker.DB.addUserInTable(user);
        }
        return user;
    }

    public static List<Author> getAllAuthorById(List<Integer> arr) {
        List<Author> authorsL = new ArrayList<>();
        for (Author author : authors) {
            if (arr.contains(author.getInDB()))
                authorsL.add(author);
        }
        return authorsL;
    }


    public static Author getAuthorById(int idInDB) {
        for (Author author : authors)
            if (author.getInDB() == idInDB)
                return author;
        return null;
    }

    public static IBook getBookById(int idInDB) {
        for (IBook book : books)
            if (book.getIdInDB() == idInDB)
                return book;
        return null;
    }

    public static IUser getUserByVkId(int idInVk) {
        for (IUser user : users)
            if (user.getTypeID() == idInVk)
                return user;
        return null;
    }

    public static String findWord(String parse, String startWith, String endWith) {
        int first = parse.indexOf(startWith);
        return parse.substring(first + startWith.length(), parse.indexOf(endWith, first));

    }
}
