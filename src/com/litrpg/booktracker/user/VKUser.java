package com.litrpg.booktracker.user;

import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.event.AuthorUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * com.litrpg.booktracker.user
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class VKUser implements IUser {
    List<IBook> books = new ArrayList<>();
    List<Author> authors = new ArrayList<>();
    int perm = 0;
    int idInDB = -1, idInVk = -1, sizeUpdate = 0;

    public VKUser(String id, String subcr) {
        idInVk = Integer.parseInt(id.substring(id.indexOf("_") + 1));
        addAllSub(subcr);
    }

    public VKUser(User usr) {
        idInVk = usr.getId();
    }


    public VKUser addSub(IBook book) {
        if (!books.contains(book))
            books.add(book);
        BookTracker.DB.updateUser(this);
        return this;
    }

    public VKUser addSub(Author author) {
        if (!authors.contains(author))
            authors.add(author);
        BookTracker.DB.updateUser(this);
        return this;
    }

    @Override
    public IUser removeSub(IBook book) {
        for (int i = 0; i < books.size(); i++)
            if (books.get(i).getUrl().startsWith(book.getUrl()))
                books.remove(i);
        BookTracker.DB.updateUser(this);
        return this;
    }

    @Override
    public IUser removeSub(Author author) {
        for (int i = 0; i < authors.size(); i++)
            if (authors.get(i).getUrl().startsWith(author.getUrl()))
                authors.remove(i);
        BookTracker.DB.updateUser(this);
        return this;
    }

    @Override
    public UsersType getType() {
        return UsersType.VK;
    }

    @Override
    public List<IBook> getSubsBook() {
        return books;
    }

    @Override
    public List<Author> getSubsAuthor() {
        return authors;
    }

    @Override
    public int getTypeID() {
        return idInVk;
    }

    @Override
    public IUser setSizeUpdate(int sizeUpdate) {
        this.sizeUpdate = sizeUpdate;
        return this;
    }

    @Override
    public int getSizeUpdate() {
        return sizeUpdate;
    }

    @Override
    public int getPerm() {
        return perm;
    }

    @Override
    public IUser setPerm(int perm) {
        this.perm = perm;
        return this;
    }

    @Override
    public int getDBId() {
        if (idInDB < 0)
            idInDB = BookTracker.DB.getIdUser(this);
        return idInDB;
    }

    public VKUser addAllSub(String sub) {
        for (String id : sub.split(",")) {
            if (id.length() > 0) {
                int subId = Integer.parseInt(id);
                if (subId < 0) {
                    Author author = MainParser.getAuthorById(subId * -1);
                    if (!authors.contains(author))
                        authors.add(author);
                } else {
                    IBook book = MainParser.getBookById(subId);
                    if (!books.contains(book))
                        books.add(book);
                }
            }
        }
        return this;
    }


    @Override
    public String getSubAsString() {
        StringBuilder sb = new StringBuilder();
        for (IBook book : books) {
            if (sb.length() > 0) sb.append(",");
            sb.append(book.getIdInDB());
        }
        for (Author author : authors) {
            if (sb.length() > 0) sb.append(",");
            sb.append("-").append(author.getInDB());
        }
        return sb.toString();
    }

    @Override
    public boolean isBookSubscribe(IBook book) {
        return books.contains(book);
    }

    @Override
    public boolean isAuthorSubscribe(Author author) {
        return authors.contains(author);
    }

    @Override
    public void onUpdateBook(BookUpdateEvent e) {
        if (e.sizeUp > sizeUpdate) {
            new Message(e.toString()).addMedia("photo286477373_399669155").sendMessage(BookTracker.vk, idInVk);
            Logger.getLogger().print("Отправленно сообщение об обновлении книги \"" + e.book.getNameBook() + "\" пользователю vk с id" + idInVk);
        }
    }

    @Override
    public void onUpdateAuthor(AuthorUpdateEvent e) {
        new Message(e.toString()).addMedia("photo286477373_399669155").sendMessage(BookTracker.vk, idInVk);
        Logger.getLogger().print("Отправленно сообщение об обновлении автора \"" + e.getAuthor() + "\" пользователю vk с id" + idInVk);
    }

    @Override
    public IUser setIdInDB(int id) {
        idInDB = id;
        return this;
    }
}
