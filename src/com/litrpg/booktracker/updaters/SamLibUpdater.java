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

    public SamLibUpdater(Date date) throws FileNotFoundException {
        this.date = date;
        update();
    }

    public void update() throws FileNotFoundException {
        String[] tmp = new String[0];
        tmp = URLHelper.getSiteAsString(getLink(date), "Windows-1251").split("\n");

        updateList = new String[tmp.length][12];
        for (int i = 0; i < tmp.length; i++) {
            updateList[i] = Split.spilitToByte(tmp[i], (byte) 124);
        }
    }

    public BookUpdateEvent checkUpdateBook(IBook book) {
        String updUrl = book.getUrl().substring(0, book.getUrl().lastIndexOf(".shtml")).replaceFirst("http://samlib.ru", "");
        for (String[] update : updateList) {
            if ((update[0].equalsIgnoreCase(updUrl) || update[0].equalsIgnoreCase(updUrl + "/")) && Timestamp.valueOf(update[2]).getTime() - book.getLastUpdate().getTime() > 900000) {
                int size = 0;
                if (update[11].length() > 2)
                    size = Integer.parseInt(update[11].substring(0, update[11].length() - 2)) * 1000;
                Logger.getLogger().print("[SL]Обнаруженно обновление книги " + book.getNameBook() + ". Последняя проверка " + MySql.dateToString(book.getLastCheck()) + "(" + book.getLastCheck().getTime() + ").А обновление от " + update[2] + "(" + Timestamp.valueOf(update[2]).getTime() + "). В базе данных: " + book.getLastUpdate());
                BookUpdateEvent e = Updater.subscribe.getBookEvent(book, size, Timestamp.valueOf(update[2]));
                book.setAnnotation(update[7]);
                return e;
            }
        }
        return null;
    }

    public AuthorUpdateEvent checkUpdateAuthor(Author author) {
        String updUrl = author.getUrl().replaceFirst("http://samlib.ru", "");
        for (String[] update : updateList) {
            if (update[0].equalsIgnoreCase(updUrl) || update[0].equalsIgnoreCase(updUrl + "/"))
                if (Timestamp.valueOf(update[2]).getTime() - author.getLastUpdate().getTime() > 900000) {
                    try {
                        Logger.getLogger().print("[SL]Обнаруженно обновление автора " + author.getName() + ". Последняя проверка " + MySql.dateToString(author.getLastCheck()) + ".А обновление от " + update[2] + ".В базе данных: " + author.getLastUpdate());
                        int size = 0;
                        if (update[11].length() > 2)
                            size = Integer.parseInt(update[11].substring(0, update[11].length() - 2)) * 1000;
                        AuthorUpdateEvent e = Updater.subscribe.getAuthorEvent(author, MainParser.getBook("http://samlib.ru" + update[0] + ".shtml"), Timestamp.valueOf(update[2]), size);
                        return e;
                    } catch (FileNotFoundException e) {
                        Logger.getLogger().print("Книга " + update[0] + " по видимому, была удалена.");
                    }
                }
        }
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

    public Date getLastUpdate(String updUrl) {
        for (int i = updateList.length - 1; i > -1; i--) {
            if (updateList[i][0].equalsIgnoreCase(updUrl) || updateList[i][0].equalsIgnoreCase(updUrl + "/"))
                return Timestamp.valueOf(updateList[i][2]);
        }
        return null;
    }
}
