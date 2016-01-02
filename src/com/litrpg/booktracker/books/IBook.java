package com.litrpg.booktracker.books;

import com.litrpg.booktracker.authors.Author;

import java.util.Date;

/**
 * com.litrpg.booktracker.books
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public interface IBook {

    TypeSite getType();

    String getNameBook();

    Author getAuthor();

    String getAnnotation();

    String getUrl();

    Date getLastUpdate();

    Date getLastChecked();

    int getSize();
}
