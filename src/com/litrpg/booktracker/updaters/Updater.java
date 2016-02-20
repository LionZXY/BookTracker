package com.litrpg.booktracker.updaters;

import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.parsers.LitEraParser;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.event.AuthorUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateSubscribe;
import com.litrpg.booktracker.updaters.event.IBookUpdateListiner;
import com.litrpg.booktracker.user.Users;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;

/**
 * com.litrpg.booktracker.updaters.event
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class Updater implements IBookUpdateListiner {

    public static final int minSizeUp = 0;

    public static BookUpdateSubscribe subscribe = new BookUpdateSubscribe();
    public static LitEraUpdater litera = new LitEraUpdater();
    public static HashMap<Date, SamLibUpdater> samlib = new HashMap<>();

    static {
        subscribe.subscribe(new Updater());
        subscribe.subscribe(new Users());
        long t = System.currentTimeMillis();
        Date tmp = new Date(t - t % 86400000);
        samlib.put(tmp, new SamLibUpdater(tmp));
    }

    public static void checkAllBooks() {
        litera = new LitEraUpdater();
        getSamlib(new Date()).update();
        Logger.getLogger().print("Проверка книг началась. На очереди " + MainParser.books.size() + " книг");
        MainParser.books.forEach(Updater::checkBook);
        BookTracker.DB.checkNowBook();
        Logger.getLogger().print("Всё книги проверенны!");
    }

    public static void checkAllAuthor() {
        Logger.getLogger().print("Проверка авторов началась. На очереди " + MainParser.authors.size() + " авторов");
        for (int i = 0; i < MainParser.authors.size(); i++)
            checkAuthor(MainParser.authors.get(i));
        BookTracker.DB.checkNowAuthor();
        Logger.getLogger().print("Всё авторы проверенны!");
    }

    public static void checkBook(IBook book) {
        BookUpdateEvent event = null;
        //Logger.getLogger().print("Проверка на обновление книги " + book.getNameBook());

        switch (book.getType()) {
            case LITERA:
                event = litera.checkUpdateBook(book);
                break;
            case SAMLIB:
                for (Date d : SamLibUpdater.getDaysFrom(book.getLastCheck(), new Date())) {
                    event = getSamlib(d).checkUpdateBook(book);
                    if (event != null)
                        break;
                }
                if (event == null)
                    event = getSamlib(new Date()).checkUpdateBook(book);
        }
        book.setLastCheck(new Date());
        if (event != null) {
            book.setLastUpdate(event.updateTime);
            Logger.getLogger().print("Обнаруженно обновление книги " + book.getNameBook() + " от " + event.updateTime);
            subscribe.sendUpdateEvent(event);
        }
    }

    public static void checkAuthor(Author author) {
        AuthorUpdateEvent event = null;
        switch (author.getTypeSite()) {
            case LITERA:
                event = litera.checkUpdateAuthor(author);
                break;
            case SAMLIB:
                for (Date d : SamLibUpdater.getDaysFrom(author.getLastCheck(), new Date())) {
                    event = getSamlib(d).checkUpdateAuthor(author);
                    if (event != null)
                        break;
                }
                if (event == null)
                    event = getSamlib(new Date()).checkUpdateAuthor(author);
        }
        author.setLastCheck(new Date());
        if (event != null) {
            author.setLastUpdate(event.getUpdateTime());
            Logger.getLogger().print("Обнаруженно обновление автора " + author.getName() + " от " + event.getUpdateTime());
            subscribe.sendUpdateEvent(event);
        }
    }

    @Override
    public void bookUpdate(BookUpdateEvent e) {
        String annotation = null;
        switch (e.book.getType()) {
            case LITERA:
                try {
                    annotation = new LitEraParser(e.book.getUrl()).getAnnotation();
                } catch (FileNotFoundException ex) {
                    annotation = "Книга была удалена или временно недоступна";
                }
                e.book.setAnnotation(annotation).setLastUpdate(e.updateTime).setSize(e.sizeUp + e.book.getSize()).setLastCheck(new Date());
            default:
                break;
        }

        System.out.println(e);
    }

    @Override
    public void authorUpdate(AuthorUpdateEvent e) {
        e.getAuthor().setLastUpdate(e.getUpdateTime()).setLastCheck(new Date());
    }

    public static SamLibUpdater getSamlib(Date date) {
        Date day = new Date(date.getTime() - date.getTime() % 86400000);
        SamLibUpdater samLibUpdater = samlib.get(day);
        if (samLibUpdater == null) {
            samlib.put(day, new SamLibUpdater(day));
            return getSamlib(day);
        }
        return samLibUpdater;
    }
}
