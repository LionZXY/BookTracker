package com.litrpg.booktracker.user;

import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.parsers.MainParser;
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
    int idInDB = -1, idInVk = -1;

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
                    IBook book = MainParser.getBookById(idInDB);
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
        return false;
    }

    @Override
    public void onUpdateBook(BookUpdateEvent e) {
        new Message(e.toString()).addMedia("photo286477373_399669155").sendMessage(BookTracker.vk, idInVk);
        Logger.getLogger().print("Отправленно сообщение об обновлении книги \"" + e.book.getNameBook() + "\" пользователю vk с id" + idInVk);
    }

    @Override
    public IUser setIdInDB(int id) {
        idInDB = id;
        return this;
    }
}
