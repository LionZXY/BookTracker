package com.litrpg.booktracker.authors;

import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.updaters.IUpdateObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.litrpg.booktracker.authors
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class Author implements IUpdateObject{
    private String name, url = "";
    private TypeSite typeSite;
    Date lastCheck = null;
    Date lastUpdate = null;
    List<IBook> books = new ArrayList<>();
    int inDB = -1;

    public Author(String name, String url) {
        this.name = name;
        this.url = url;
        typeSite = TypeSite.getTypeFromUrl(url);
    }

    public Author setTypeSite(TypeSite typeSite) {
        this.typeSite = typeSite;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        if (!url.startsWith(typeSite.getSite()))
            return typeSite.getSite() + url;
        return url;
    }

    public void addBook(IBook book) {
        if (!books.contains(book)) {
            books.add(book);
            BookTracker.DB.updateAuthorBook(this);
        }
    }

    public String getBooks() {
        StringBuilder toExit = new StringBuilder();
        for (IBook book : books) {
            if (toExit.length() != 0) toExit.append(",");
            toExit.append(book.getIdInDB());
        }
        return toExit.toString();
    }

    public TypeSite getTypeSite() {
        return typeSite;
    }

    public int getInDB() {
        if (inDB == -1) {
            inDB = BookTracker.DB.getIdAuthor(this);
        }
        return inDB;
    }

    public Author setIdDB(int id) {
        this.inDB = id;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

    public static String getAsString(List<Author> authors) {
        StringBuilder sb = new StringBuilder();
        for (Author author : authors) {
            if (sb.length() > 1) sb.append(",");
            sb.append(author.getName());
        }
        return sb.toString();
    }


    @Override
    public Date getLastCheck() {
        if(lastCheck == null)
            return new Date();
        return lastCheck;
    }

    public Author setLastCheck(Date lastCheck) {
        this.lastCheck = lastCheck;
        return this;
    }

    @Override
    public Date getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public Author setLastUpdate(Date date) {
        this.lastUpdate = date;
        return this;
    }
}
