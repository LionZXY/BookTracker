package com.litrpg.booktracker.parsers;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.Book;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.helper.URLHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * com.litrpg.booktracker.parsers
 * Created by LionZXY on 12.01.2016.
 * BookTracker
 */
public class LitEraParser {
    String html = null;
    String url = null;

    public LitEraParser(String url) {
        this.url = url;
        this.html = URLHelper.getSiteAsString(url, "utf8");
    }

    public IBook parseBook() {
        return new Book(TypeSite.LITERA, getName(), getAnnotation(), url, getAuthor(), getDateEdit(), getGenres(), getBookSize());

    }

    public String getAnnotation() {
        return findWord("<h4>Аннотация:</h4>", "</div>").replaceAll("<br />", "\n").replaceFirst("                                            ", "");
    }

    public Date getDateEdit() {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(findWord("<p class=\"last-edit\">Отредактировано: ", "</p>"));
        } catch (ParseException e) {
            return null;
        }
    }

    public Genres[] getGenres() {
        List<Genres> genres = new ArrayList<Genres>();

        for (String genre : findWord("<p><span class=\"meta-name\">Жанр: </span> ", "</p>").split(", "))
            genres.add(Genres.getGenre(findWord(genre, "'>", "</a>")));

        return genres.toArray(new Genres[genres.size()]);
    }

    public int getBookSize() {
        return Integer.parseInt(findWord("<p><span class=\"meta-name\">Размер: </span> ", " знаков</p>").replaceAll(" ", ""));

    }

    public Author getAuthor() {
        int first = html.indexOf("\" class=\"author\">");
        Author author = new Author(html.substring(first + " class=\"author\">".length() + 1, html.indexOf("</a>", first)));
        author.setURL(html.substring(html.substring(0, first).lastIndexOf("\"") + 1, first));
        author.setTypeSite(TypeSite.LITERA);
        return author;
    }

    public String getName() {
        return findWord("<h1 class=\"narrow\">", "</h1>");
    }

    public String findWord(String startWith, String endWith) {
        return findWord(html, startWith, endWith);
    }

    public static String findWord(String parse, String startWith, String endWith) {
        int first = parse.indexOf(startWith);
        return parse.substring(first + startWith.length(), parse.indexOf(endWith, first));
    }
}
