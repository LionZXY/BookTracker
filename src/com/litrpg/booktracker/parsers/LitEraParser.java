package com.litrpg.booktracker.parsers;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.helper.URLHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * com.litrpg.booktracker.parsers
 * Created by LionZXY on 12.01.2016.
 * BookTracker
 */
public class LitEraParser {
    String html = null;

    public LitEraParser(String url) {
        this.html = URLHelper.getSiteAsString(url, "utf8");
    }

    public IBook parseBook() {
        getName();
        getAuthor();
        System.out.println(getBookSize());
        getGenres();
        System.out.println(getDateEdit());
        System.out.println(getAnnotation());
        return null;
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

    public static String getText(String url) {
        List<Integer> partValue = new ArrayList<>();
        for (String part : new LitEraParser(url).findWord("<option value=\"\">Содержание</option>", "</select>").replaceFirst("                                                                                                            ", "").split("                                                                                                                                                "))
            partValue.add(Integer.parseInt(findWord(part, "<option value=\"", "\">")));
        StringBuilder book = new StringBuilder();
        for (Integer part : partValue) {
            String html = URLHelper.getSiteAsString(url.replaceFirst("https://lit-era.com/book/", "https://lit-era.com/reader/") + "?c=" + part, "utf8");
            String workOnText = temlareText(findWord(html, "<div class=\"reader-text font-size-medium\"", "</div>"));
            workOnText = workOnText.substring(workOnText.indexOf("</h2>") + "</h2>".length() + 1)
                    .replaceAll("</p>", "\n\n")
                    .replaceAll("&mdash;", "-")
                    .replaceAll("<p>", "")
                    .replaceAll("&nbsp;", "\n")
                    .replaceAll("&hellip;", "...")
                    .replaceAll("<br />", "\n")
                    .replaceAll("<strong>","")
                    .replaceAll("</strong>","")
                    .replaceAll("&ndash;","-");
            book.append(workOnText);
        }
        System.out.println(book);
        return null;
    }

    public static String temlareText(String s) {
        while (s.contains("<p style=")) {
            int found = s.indexOf("<p style=\"");
            s = s.replaceAll(s.substring(found, s.indexOf("\">", found)) + 2, "");
        }
        return s;
    }
}
