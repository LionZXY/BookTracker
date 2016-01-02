package com.litrpg.booktracker.books;

import com.sun.istack.internal.NotNull;

/**
 * com.litrpg.booktracker.books
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public enum TypeSite {
    SAMLIB("http://samlib.ru/", "samlib");
    private String site, nameInDB;

    TypeSite(String site, @NotNull String nameInDB) {
        this.site = site;
        this.nameInDB = nameInDB;
    }

    public static TypeSite getBookFromNameInDB(String nameInDB) {
        for (TypeSite book : TypeSite.values())
            if (book.getNameInBD().equalsIgnoreCase(nameInDB))
                return book;
        return null;
    }

    public String getSite() {
        return site;
    }

    public String getNameInBD() {
        return nameInDB;
    }


}
