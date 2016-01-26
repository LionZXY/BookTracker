package com.litrpg.booktracker.updaters.event;

import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.mysql.MySql;

import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;

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

    @Override
    public String toString() {
        return "#прода@litrpg_book\n\n" + book.toString() + "Добавленно продолжение от " + MySql.dateToString(updateTime) + ": " + book.getSize() / 1000 + "К (" + "+" + sizeUp + " Знаков).\n\n" + book.getUrl();
    }
}


