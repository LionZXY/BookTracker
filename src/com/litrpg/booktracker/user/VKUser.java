package com.litrpg.booktracker.user;

import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.parsers.MainParser;

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
    int idInDB = -1, idInVk = -1;

    public VKUser(String id, String subcr) {
        idInVk = Integer.parseInt(id.substring(0, id.indexOf("_") + 1));
        addAllSub(subcr);
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
    public int getTypeID() {
        return idInVk;
    }

    @Override
    public int getDBId() {
        if (idInDB < 0)
            idInDB = BookTracker.DB.getIdUser(this);
        return idInDB;
    }

    public VKUser addAllSub(String sub) {
        for (String id : sub.split(",")) {
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
    public IUser setIdInDB(int id) {
        idInDB = id;
        return this;
    }
}
