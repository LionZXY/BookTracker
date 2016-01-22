package com.litrpg.booktracker.user;

import com.litrpg.booktracker.books.IBook;

/**
 * com.litrpg.booktracker.user
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public interface IUser {
    UsersType getType();

    int getTypeID();

    int getDBId();

    String getSubAsString();

    boolean isBookSubscribe(IBook book);

    IUser setIdInDB(int id);
}
