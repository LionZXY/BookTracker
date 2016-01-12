package com.litrpg.booktracker.books;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;

import java.util.Date;

/**
 * com.litrpg.booktracker.books
 * Created by LionZXY on 12.01.2016.
 * BookTracker
 */
public class Book implements IBook {
    TypeSite typeSite;
    String nameBook;
    String annotation;
    String url;
    Author author;
    Date lastUpdate;
    Date lastChecked = null;
    Genres[] genres;
    int bookSize;

    public Book(TypeSite typeSite, String nameBook, String annotation, String url, Author author, Date lastUpdate, Genres[] genres, int bookSize) {
        this.typeSite = typeSite;
        this.nameBook = nameBook;
        this.annotation = annotation;
        this.url = url;
        this.author = author;
        this.lastUpdate = lastUpdate;
        this.genres = genres;
        this.bookSize = bookSize;
    }

    @Override
    public TypeSite getType() {
        return typeSite;
    }

    @Override
    public String getNameBook() {
        return nameBook;
    }

    @Override
    public Author getAuthor() {
        return author;
    }

    @Override
    public String getAnnotation() {
        return annotation;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Date getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public Date getLastChecked() {
        if (lastChecked == null)
            return getLastUpdate();
        return lastChecked;
    }

    @Override
    public Genres[] getGenres() {
        return genres;
    }

    @Override
    public int getSize() {
        return bookSize;
    }
}
