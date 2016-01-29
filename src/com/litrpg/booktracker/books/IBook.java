package com.litrpg.booktracker.books;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;

import java.util.Date;
import java.util.List;

/**
 * com.litrpg.booktracker.books
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public interface IBook {

    TypeSite getType();
    String getNameBook();
    List<Author> getAuthors();
    String getAnnotation();
    String getUrl();
    Date getLastUpdate();
    int getIdInDB();
    Date getLastCheck();
    List<Genres> getGenres();
    int getSize();

    IBook setDBid(int id);
    IBook setLastCheck(Date date);
    IBook setAnnotation(String annotation);
    IBook setLastUpdate(Date lastUpdate);
    IBook setSize(int size);

}
