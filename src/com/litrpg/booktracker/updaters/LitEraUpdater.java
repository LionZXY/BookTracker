package com.litrpg.booktracker.updaters;

import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.helper.URLHelper;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.event.AuthorUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * com.litrpg.booktracker.updaters
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class LitEraUpdater {

    JSONArray jsonArray = new JSONArray();

    public LitEraUpdater() {
        try {
            jsonArray = (JSONArray) new JSONParser().parse(new StringReader(URLHelper.getSiteAsString("https://lit-era.com/api/updated-books", "utf8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BookUpdateEvent checkUpdateBook(IBook book) {
        for (Object bookJson : jsonArray) {
            if (bookJson instanceof JSONObject && ((String) ((JSONObject) bookJson).get("url")).equalsIgnoreCase(book.getUrl())) {
                if (stringToDate((String) ((JSONObject) bookJson).get("updated_at")).getTime() - book.getLastUpdate().getTime() > 900000)
                    if (Math.toIntExact((Long) ((JSONObject) bookJson).get("chr_length")) - book.getSize() > Updater.minSizeUp) {
                        Logger.getLogger().print("[LE]Обнаруженно обновление книги " + book.getNameBook() + ". Последняя проверка " + MySql.dateToString(book.getLastCheck()) + ".А обновление от " + ((JSONObject) bookJson).get("updated_at") + ".В базе данных: " + book.getLastUpdate());
                        BookUpdateEvent bookUpdateEvent = Updater.subscribe.getBookEvent(book, Math.toIntExact((Long) ((JSONObject) bookJson).get("chr_length")) - book.getSize(), stringToDate((String) ((JSONObject) bookJson).get("updated_at")));
                        return bookUpdateEvent;
                    }
            }
        }
        return null;
    }

    public AuthorUpdateEvent checkUpdateAuthor(Author author) {
        for (Object updateJson : jsonArray) {
            if (updateJson instanceof JSONObject
                    && ((String) ((JSONObject) updateJson).get("author")).startsWith(author.getName())
                    && stringToDate((String) ((JSONObject) updateJson).get("updated_at")).getTime() - author.getLastUpdate().getTime() > 900000) {
                try {
                    Logger.getLogger().print("[LE]Обнаруженно обновление автора " + author.getName() + ". Последняя проверка " + MySql.dateToString(author.getLastCheck()) + ".А обновление от " + ((JSONObject) updateJson).get("updated_at") + ".В базе данных: " + author.getLastUpdate());
                    IBook book = MainParser.getBook((String) ((JSONObject) updateJson).get("url"));
                    if (book.getAuthors().contains(author))
                        return Updater.subscribe.getAuthorEvent(author, book, stringToDate((String) ((JSONObject) updateJson).get("updated_at")));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger().print("Книга " + ((JSONObject) updateJson).get("url") + " по видимому, была удалена.");
                }
            }
        }
        return null;
    }

    public static Date stringToDate(String date) {
        //YYYY-MM-DD HH:MM:SS
        return Timestamp.valueOf(date);
    }

    public Date getLastUpdate(String url){
        for(Object bJ : jsonArray){
            if (bJ instanceof JSONObject && ((String) ((JSONObject) bJ).get("url")).equalsIgnoreCase(url)) {
                return stringToDate((String) ((JSONObject) bJ).get("updated_at"));
            }
        }
        return null;
    }
}
