package com.litrpg.booktracker.updaters.event;

import com.litrpg.booktracker.books.IBook;

import java.util.Date;
import java.util.EventObject;

/**
 * com.litrpg.booktracker.updaters.event
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class BookUpdateEvent extends EventObject {
    public final IBook book;
    public final int sizeUp;
    public final Date updateTime;

    public BookUpdateEvent(Object object, IBook book, int sizeUp, Date updateDate) {
        super(object);
        this.book = book;
        this.sizeUp = sizeUp;
        this.updateTime = updateDate;
    }
}


