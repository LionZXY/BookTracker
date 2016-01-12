package com.litrpg.booktracker.authors;

import com.litrpg.booktracker.enums.TypeSite;

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
        if (!url.startsWith(typeSite.getSite()))
            return typeSite.getSite() + url;
        return url;
    }

    public TypeSite getTypeSite() {
        return typeSite;
    }

    @Override
    public String toString() {
        return name + "\n" + url + "\n" + typeSite;
    }
}
