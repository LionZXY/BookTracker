package com.litrpg.booktracker.parsers;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.Book;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.helper.URLHelper;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.litrpg.booktracker.parsers
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class SamLibParser extends MainParser {
    public String html;
    public String url;

    public SamLibParser(String url) throws FileNotFoundException{
        this.url = url.replaceAll("editors/", "");
        if (TypeSite.getTypeFromUrl(url) == TypeSite.SAMLIB) {
            html = URLHelper.getSiteAsString(url, "Windows-1251");
        }
    }

    public IBook parseBook() {
        IBook book = MainParser.findBook(url);
        if (book == null) {
            book = new Book(TypeSite.SAMLIB, getName(), getAnnotation(), url, getAuthors(), getDateEdit(), getGenres(), getBookSize());
            book.setLastCheck(new Date());
            MainParser.addBook(book);
        }
        return book;
    }

    public String getName() {
        return findWord(html, "<center><h2>", "</h2>");
    }

    public String getAnnotation() {
        try {
            return findWord(html, "<ul><small><li></small><b>Аннотация:</b><br><font color=\"#555555\"><i>", "</i></font></ul>");
        } catch (Exception e) {
            return "";
        }
    }

    public Author getAuthor() {
        String tmp = findWord(html, "<h3>", "<br>");
        Author author = new Author(tmp.substring(0, tmp.length() - 1), url);
        author.setLastCheck(new Date());
        author.setLastUpdate(new Date());
        MainParser.addAuthor(author);
        return author;
    }

    public List<Author> getAuthors() {
        int first = html.indexOf("<li>&copy; Copyright <a href=") + 29;
        String url = "http://samlib.ru" + html.substring(first, html.indexOf(">", first));
        Author author = MainParser.findAuthor(url);
        if (author == null) {
            author = new Author(html.substring(first - 16 + url.length(), html.indexOf("</a>", first)), url);
            author.setLastUpdate(new Date());
            author.setLastCheck(new Date());
            author.setTypeSite(TypeSite.SAMLIB);
            MainParser.addAuthor(author);
        }
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        return authors;
    }

    public int getBookSize() {
        String dataBlock = findWord(html, "<li>Размещен: ", " <a href=");
        String size = dataBlock.substring(dataBlock.lastIndexOf(" ") + 1, dataBlock.lastIndexOf("k"));
        return Integer.parseInt(size) * 1000;
    }

    public List<Genres> getGenres() {
        List<Genres> genresList = new ArrayList<>();
        int first = html.indexOf("</a>: <a href=\"") + 6;
        String genres = html.substring(first, html.indexOf("<li>", first));
        for (String genre : genres.split(", ")) {
            genresList.add(Genres.getGenre(findWord(genre, ">", "<")));
        }
        return genresList;
    }

    public Date getDateEdit() {
        String dataBlock = findWord(html, "<li>Размещен: ", " <a href=");
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(findWord(dataBlock, ", изменен: ", ". "));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
