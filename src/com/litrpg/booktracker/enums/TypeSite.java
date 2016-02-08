package com.litrpg.booktracker.enums;

import com.sun.istack.internal.NotNull;

/**
 * com.litrpg.booktracker.books
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public enum TypeSite {
    SAMLIB("http://samlib.ru/", "samlib", "Самиздат"),
    LITERA("https://lit-era.com/", "litera", "Лит-Эра"),
    Unknown("UNKNOWN SITE", "unknown", "Неизвестно");
    private String site, nameInDB,russ;

    TypeSite(String site, @NotNull String nameInDB, String russ) {
        this.site = site;
        this.nameInDB = nameInDB;
        this.russ = russ;
    }

    public static TypeSite getTypeFromNameInDB(String nameInDB) {
        for (TypeSite book : TypeSite.values())
            if (book.getNameInBD().equalsIgnoreCase(nameInDB))
                return book;
        return Unknown;
    }

    public static TypeSite getTypeFromUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        for (TypeSite book : TypeSite.values())
            if (url.startsWith(book.getSite()))
                return book;
        return Unknown;
    }

    public String getSite() {
        return site;
    }

    public String getNameInBD() {
        return nameInDB;
    }

    public String getRuss(){return russ;}

    @Override
    public String toString() {
        return getNameInBD();
    }
}
