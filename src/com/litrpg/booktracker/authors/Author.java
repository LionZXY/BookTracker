package com.litrpg.booktracker.authors;

import com.litrpg.booktracker.books.TypeSite;

/**
 * com.litrpg.booktracker.authors
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class Author {
    private String name, url;
    private TypeSite typeSite;

    public Author(String name) {
        this.name = name;
    }

    public Author setURL(String url) {
        this.url = url;
        return this;
    }

    public Author setTypeSite(TypeSite typeSite) {
        this.typeSite = typeSite;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public TypeSite getTypeSite() {
        return typeSite;
    }

}
