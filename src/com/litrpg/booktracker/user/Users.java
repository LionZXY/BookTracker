package com.litrpg.booktracker.user;

import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.event.AuthorUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;
import com.litrpg.booktracker.updaters.event.IBookUpdateListiner;

/**
 * com.litrpg.booktracker.user
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class Users implements IBookUpdateListiner {
    @Override
    public void bookUpdate(BookUpdateEvent e) {
        for (IUser user : MainParser.users) {
            if (user.isBookSubscribe(e.book))
                user.onUpdateBook(e);
        }
    }

    @Override
    public void authorUpdate(AuthorUpdateEvent e) {
        for (IUser user : MainParser.users) {
            if (user.isAuthorSubscribe(e.getAuthor()))
                user.onUpdateAuthor(e);
        }
    }
}
