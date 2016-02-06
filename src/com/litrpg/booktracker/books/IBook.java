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
    String getAnnotation();
    String getUrl();
    String getPhotoUrl();
    String getNameBook();
    int getIdInDB();
    int getSize();
    Date getLastCheck();
    Date getLastUpdate();
    List<Genres> getGenres();
    List<Author> getAuthors();

    IBook setPhotoUrl(String url);
    IBook setDBid(int id);
    IBook setLastCheck(Date date);
    IBook setAnnotation(String annotation);
    IBook setLastUpdate(Date lastUpdate);
    IBook setSize(int size);

}
