package com.litrpg.booktracker.user;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;

import java.util.List;

/**
 * com.litrpg.booktracker.user
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public interface IUser {
    UsersType getType();

    List<IBook> getSubsBook();
    List<Author> getSubsAuthor();
    int getTypeID();
    int getPerm();
    IUser setPerm(int perm);

    IUser addSub(IBook book);

    int getDBId();

    String getSubAsString();

    boolean isBookSubscribe(IBook book);

    void onUpdateBook(BookUpdateEvent e);

    IUser setIdInDB(int id);
}
