package com.litrpg.booktracker.updaters.event;

/**
 * com.litrpg.booktracker.updaters.event
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public interface IBookUpdateListiner {

    void bookUpdate(BookUpdateEvent e);

    void authorUpdate(AuthorUpdateEvent e);
}
