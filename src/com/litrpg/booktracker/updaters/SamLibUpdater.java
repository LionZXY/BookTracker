package com.litrpg.booktracker.updaters;

import com.lionzxy.core.string.Split;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.helper.URLHelper;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.event.AuthorUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.litrpg.booktracker.updaters
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class SamLibUpdater {
    String[][] updateList;
    Date date;
    //12

    public SamLibUpdater(Date date) {
        this.date = date;
        update();
    }

    public void update() {
        String[] tmp = new String[0];
        try {
            tmp = URLHelper.getSiteAsString(getLink(date), "Windows-1251").split("\n");
        } catch (FileNotFoundException e) {
            this.update();
        }

        updateList = new String[tmp.length][12];
        for (int i = 0; i < tmp.length; i++) {
            updateList[i] = Split.spilitToByte(tmp[i], (byte) 124);
        }
    }

    public BookUpdateEvent checkUpdateBook(IBook book) {
        String updUrl = book.getUrl().substring(0, book.getUrl().lastIndexOf(".shtml")).replaceFirst("http://samlib.ru", "");
        for (String[] update : updateList) {
            if (update[0].equalsIgnoreCase(updUrl) && Timestamp.valueOf(update[2]).getTime() > book.getLastCheck().getTime()) {
                int size = 0;
                if (update[11].length() > 2)
                    size = Integer.parseInt(update[11].substring(0, update[11].length() - 2)) * 1000;
                Logger.getLogger().print("Обнаруженно обновление книги. Последнее проверка " + MySql.dateToString(book.getLastCheck()) + ".А обновление от " + update[2]);
                BookUpdateEvent e = Updater.subscribe.getBookEvent(book, size, Timestamp.valueOf(update[2]));
                book.setAnnotation(update[7]);
                book.setLastUpdate(Timestamp.valueOf(update[2]));
                return e;
            }
        }
        book.setLastCheck(new Date());
        return null;
    }

    public AuthorUpdateEvent checkUpdateAuthor(Author author) {
        String updUrl = author.getUrl().replaceFirst("http://samlib.ru", "");
        for (String[] update : updateList) {
            if (update[0].startsWith(updUrl))
                if (Timestamp.valueOf(update[2]).getTime() > author.getLastCheck().getTime()) {
                    try {
                        Logger.getLogger().print("Обнаруженно обновление книги. Последнее проверка " + MySql.dateToString(author.getLastCheck()) + ".А обновление от " + update[2]);
                        int size = 0;
                        if (update[11].length() > 2)
                            size = Integer.parseInt(update[11].substring(0, update[11].length() - 2)) * 1000;
                        AuthorUpdateEvent e = Updater.subscribe.getAuthorEvent(author, MainParser.getBook("http://samlib.ru" + update[0] + ".shtml"), Timestamp.valueOf(update[2]), size);
                        author.setLastUpdate(Timestamp.valueOf(update[2]));
                        return e;
                    } catch (FileNotFoundException e) {
                        Logger.getLogger().print("Книга " + update[0] + " по видимому, была удалена.");
                    }
                }
        }
        author.setLastCheck(new Date());
        return null;
    }

    public static List<Date> getDaysFrom(Date from, Date to) {
        List<Date> dates = new ArrayList<>();
        for (long i = from.getTime(); i < to.getTime(); i += 86400000)
            dates.add(new Date(i));
        return dates;
    }

    public static String getLink(Date date) {
        CalendarDate calendarDate = BaseCalendar.getGregorianCalendar().getCalendarDate(date.getTime());
        String mouth;
        int m = calendarDate.getMonth();
        if (m < 10)
            mouth = "0" + m;
        else mouth = String.valueOf(m);
        String day;
        int d = calendarDate.getDayOfMonth();
        if (d < 10)
            day = "0" + d;
        else day = String.valueOf(d);
        return "http://samlib.ru/logs/" + calendarDate.getYear() + "/" + mouth + "-" + day + ".log";
    }
}
