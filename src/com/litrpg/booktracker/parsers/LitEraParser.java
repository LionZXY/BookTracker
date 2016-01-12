package com.litrpg.booktracker.parsers;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.helper.URLHelper;

/**
 * com.litrpg.booktracker.parsers
 * Created by LionZXY on 12.01.2016.
 * BookTracker
 */
public class LitEraParser {
    public static IBook parseBook(String url) {
        String html = URLHelper.getSiteAsString("https://lit-era.com/book/tron-znaniya-kniga-1-b3818", "utf8");
        getName(html);
        getAuthor(html);
        System.out.println(getBookSize(html));
        return null;
    }

    public static int getBookSize(String html) {
        return Integer.parseInt(findWord(html, "<p><span class=\"meta-name\">Размер: </span> ", " знаков</p>").replaceAll(" ", ""));

    }

    public static Author getAuthor(String html) {
        int first = html.indexOf("\" class=\"author\">");
        Author author = new Author(html.substring(first + " class=\"author\">".length() + 1, html.indexOf("</a>", first)));
        author.setURL(html.substring(html.substring(0, first).lastIndexOf("\"") + 1, first));
        author.setTypeSite(TypeSite.LITERA);
        return author;
    }

    public static String getName(String html) {
        return findWord(html, "<h1 class=\"narrow\">", "</h1>");
    }

    public static String findWord(String html, String startWith, String endWith) {
        int first = html.indexOf(startWith);
        return html.substring(first + startWith.length(), html.indexOf(endWith, first));
    }
}
