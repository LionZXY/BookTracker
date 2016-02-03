package com.litrpg.booktracker.updaters;

import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.parsers.LitEraParser;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.event.AuthorUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateSubscribe;
import com.litrpg.booktracker.updaters.event.IBookUpdateListiner;
import com.litrpg.booktracker.user.Users;

/**
 * com.litrpg.booktracker.updaters.event
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class Updater implements IBookUpdateListiner {

    public static final int minSizeUp = 0;

    public static BookUpdateSubscribe subscribe = new BookUpdateSubscribe();
    public static LitEraUpdater litera = new LitEraUpdater();

    static {
        subscribe.subscribe(new Updater());
        subscribe.subscribe(new Users());
    }

    public static void checkAllBooks() {
        litera = new LitEraUpdater();
        Logger.getLogger().print("Проверка книг началась. На очереди " + MainParser.books.size() + " книг");
        MainParser.books.forEach(Updater::checkBook);
        Logger.getLogger().print("Всё книги проверенны!");
    }

    public static void checkAllAuthor() {
        Logger.getLogger().print("Проверка авторов началась. На очереди " + MainParser.authors.size() + " книг");
        MainParser.authors.forEach(Updater::checkAuthor);
        Logger.getLogger().print("Всё авторы проверенны!");
    }

    public static void checkBook(IBook book) {
        BookUpdateEvent event = null;
        switch (book.getType()) {
            case LITERA:
                event = litera.checkUpdateBook(book);
                break;
        }
        if (event != null) {
            Logger.getLogger().print("Обнаруженно обновление книги " + book.getNameBook() + " от " + event.updateTime);
            subscribe.sendUpdateEvent(event);
        }
    }

    public static void checkAuthor(Author author) {
        AuthorUpdateEvent event = null;
        switch (author.getTypeSite()) {
            case LITERA:
                event = litera.checkUpdateAuthor(author);
        }
        if (event != null) {
            Logger.getLogger().print("Обнаруженно обновление автора " + author.getName() + " от " + event.getUpdateTime());
            subscribe.sendUpdateEvent(event);
        }
    }

    @Override
    public void bookUpdate(BookUpdateEvent e) {
        String annotation = null;
        switch (e.book.getType()) {
            case LITERA:
                annotation = new LitEraParser(e.book.getUrl()).getAnnotation();
                e.book.setAnnotation(annotation).setLastUpdate(e.updateTime).setSize(e.sizeUp + e.book.getSize());
        }

        System.out.println(e);
    }

    @Override
    public void authorUpdate(AuthorUpdateEvent e) {
        e.getAuthor().setLastUpdate(e.getUpdateTime());
    }
}
