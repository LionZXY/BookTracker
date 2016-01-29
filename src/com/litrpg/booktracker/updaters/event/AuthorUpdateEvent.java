package com.litrpg.booktracker.updaters.event;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.Book;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.updaters.LitEraUpdater;

import java.util.Date;
import java.util.EventObject;

/**
 * com.litrpg.booktracker.updaters.event
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class AuthorUpdateEvent extends EventObject{
    IBook book;
    Author author;
    Date updateTime;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AuthorUpdateEvent(Object source, Author author, IBook book, Date date) {
        super(source);
        this.author = author;
        this.book = book;
        this.updateTime = date;
    }

    @Override
    public String toString() {
        return "Автор " + author.getName() + " обновился!\n Его книга \"" + book.getNameBook() + "\" обновилась от " + MySql.dateToString(updateTime);
    }

    public IBook getBook(){
        return book;
    }

    public Author getAuthor(){
        return author;
    }

    public Date getUpdateTime(){
        return updateTime;
    }
}
