package com.litrpg.booktracker.user;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.updaters.event.AuthorUpdateEvent;
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

    IUser setSizeUpdate(int sizeUpdate);

    int getSizeUpdate();

    int getPerm();

    IUser setPerm(int perm);

    IUser addSub(IBook book);
    IUser addSub(Author book);
    IUser removeSub(IBook book);
    IUser removeSub(Author book);
    int getDBId();

    String getSubAsString();

    boolean isBookSubscribe(IBook book);
    boolean isAuthorSubscribe(Author author);
    void onUpdateBook(BookUpdateEvent e);
    void onUpdateAuthor(AuthorUpdateEvent e);

    IUser setIdInDB(int id);
}
