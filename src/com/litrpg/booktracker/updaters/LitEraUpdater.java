package com.litrpg.booktracker.updaters;

import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.helper.URLHelper;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.StringReader;
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
            if (bookJson instanceof JSONObject && ((String) ((JSONObject) bookJson).get("url")).startsWith(book.getUrl())) {
                  if (stringToDate((String) ((JSONObject) bookJson).get("updated_at")).getTime() > book.getLastUpdate().getTime())
                    if (Math.toIntExact((Long) ((JSONObject) bookJson).get("chr_length")) - book.getSize() > Updater.minSizeUp) {
                        BookUpdateEvent bookUpdateEvent = Updater.subscribe.getBookEvent(book, Math.toIntExact((Long) ((JSONObject) bookJson).get("chr_length")) - book.getSize(), stringToDate((String) ((JSONObject) bookJson).get("updated_at")));
                        return bookUpdateEvent;
                    }
            }
        }
        book.setLastCheck(new Date());
        return null;
    }

    public static Date stringToDate(String date) {
        //YYYY-MM-DD HH:MM:SS
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = simpleDateFormat.parse(date);
            return new Date(date1.getTime() + 3600000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
