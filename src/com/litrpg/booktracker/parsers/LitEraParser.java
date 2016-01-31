package com.litrpg.booktracker.parsers;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.Book;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.helper.URLHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.litrpg.booktracker.parsers
 * Created by LionZXY on 12.01.2016.
 * BookTracker
 */
public class LitEraParser extends MainParser {
    String html = null;
    String url = null;

    public LitEraParser(String url) {
        this.url = url;
        if (!url.startsWith("https://"))
            url = "https://" + url;
        this.html = URLHelper.getSiteAsString(url, "utf8");
    }

    public Author getAuthor() {
        try {
            return getAuthors().get(0);
        } catch (Exception e) {
            if (url.startsWith("https://lit-era.com/author/")) {
                Author author = new Author(findWord("<h1 class=\"narrow pull-left\">", " </h1>"), url);
                author.setLastUpdate(new Date());
                author.setLastCheck(new Date());
                MainParser.addAuthor(author);
                return author;
            }
        }
        return null;
    }

    public IBook parseBook() {
        IBook book = MainParser.findBook(url);
        if (book == null) {
            book = new Book(TypeSite.LITERA, getName(), getAnnotation(), url, getAuthors(), getDateEdit(), getGenres(), getBookSize());
            MainParser.addBook(book);
        }
        return book;
    }

    public String getAnnotation() {
        return removeLastSpace(findWord("<h4>Аннотация:</h4>", "</div>").replaceAll("<br />", "\n").replaceFirst("                                            ", ""));
    }

    public Date getDateEdit() {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(findWord("<p class=\"last-edit\">Отредактировано: ", "</p>"));
        } catch (ParseException e) {
            return null;
        }
    }

    public List<Genres> getGenres() {
        List<Genres> genres = new ArrayList<Genres>();

        for (String genre : findWord("<p><span class=\"meta-name\">Жанр: </span> ", "</p>").split(", "))
            genres.add(Genres.getGenre(findWord(genre, "'>", "</a>")));

        return genres;
    }

    public int getBookSize() {
        return Integer.parseInt(findWord("<p><span class=\"meta-name\">Размер: </span> ", " знаков</p>").replaceAll(" ", ""));

    }

    public List<Author> getAuthors() {
        //TODO Multiple choise
        int first = html.indexOf(" class=\"author\">");
        String url = "https://lit-era.com" + html.substring(html.substring(0, first).lastIndexOf("\"") + 1, first);
        Author author = MainParser.findAuthor(url);
        if (author == null) {
            author = new Author(html.substring(first + " class=\"author\">".length() + 1, html.indexOf("</a>", first)), url);
            author.setLastUpdate(new Date());
            author.setLastCheck(new Date());
            author.setTypeSite(TypeSite.LITERA);
            MainParser.addAuthor(author);
        }
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        return authors;
    }

    public String getName() {
        return findWord("<h1 class=\"narrow\">", "</h1>");
    }

    public String findWord(String startWith, String endWith) {
        return findWord(html, startWith, endWith);
    }



    public static String removeLastSpace(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = sb.length() - 1; i > 0; i--)
            if (sb.charAt(i) == ' ')
                sb.deleteCharAt(i);
            else return sb.toString();
        return str;
    }
}
