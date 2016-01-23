package com.litrpg.booktracker.books;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.documents.VkFile;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.util.ListHelper;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.parsers.other.ToText;

import java.util.Date;
import java.util.List;

/**
 * com.litrpg.booktracker.books
 * Created by LionZXY on 12.01.2016.
 * BookTracker
 */
public class Book implements IBook {
    TypeSite typeSite;
    String nameBook;
    String annotation;
    String url;
    List<Author> authors;
    Date lastUpdate;
    Date lastChecked = null;
    List<Genres> genres;
    int bookSize, idInDB = -1;

    public Book(TypeSite typeSite, String nameBook, String annotation, String url, List<Author> authors, Date lastUpdate, List<Genres> genres, int bookSize) {
        this.typeSite = typeSite;
        this.nameBook = nameBook;
        this.annotation = annotation;
        this.url = url;
        this.authors = authors;
        this.lastUpdate = lastUpdate;
        this.genres = genres;
        this.bookSize = bookSize;
    }

    @Override
    public TypeSite getType() {
        return typeSite;
    }

    @Override
    public String getNameBook() {
        return nameBook;
    }

    @Override
    public List<Author> getAuthors() {
        System.out.println(authors.size());
        return authors;
    }

    @Override
    public String getAnnotation() {
        return annotation;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Date getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public int getIdInDB() {
        if (idInDB == -1) {
            idInDB = BookTracker.DB.getIdBook(this);
        }
        return idInDB;
    }

    @Override
    public Date getLastChecked() {
        if (lastChecked == null)
            return getLastUpdate();
        return lastChecked;
    }

    @Override
    public List<Genres> getGenres() {
        return genres;
    }

    @Override
    public int getSize() {
        return bookSize;
    }

    @Override
    public IBook setDBid(int id) {
        idInDB = id;
        return this;
    }

    @Override
    public IBook setLastCheck(Date date) {
        this.lastChecked = date;
        return this;
    }

    @Override
    public IBook setAnnotation(String annotation) {
        this.annotation = annotation.replaceAll("\n                                                                                                ", "");
        return this;
    }

    @Override
    public IBook setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    @Override
    public IBook setSize(int size) {
        this.bookSize = size;
        return this;
    }

    public void addToMessageAsFile(VKUser vkUser, Message message) {
        message.addMedia(new VkFile(ToText.getAsFile(this), vkUser).getAsVkMedia());
    }

    @Override
    public String toString() {
        return getNameBook().toUpperCase() + "\n\n" + getAnnotation() + "Автор: " + ListHelper.getAsStringAuthorName(getAuthors()) + "\n\n";
    }
}
