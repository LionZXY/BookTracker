package com.litrpg.booktracker.updaters;

import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.parsers.LitEraParser;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateSubscribe;
import com.litrpg.booktracker.updaters.event.IBookUpdateListiner;

/**
 * com.litrpg.booktracker.updaters.event
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class Updater implements IBookUpdateListiner {

    public static final int minSizeUp = 50;

    public static BookUpdateSubscribe subscribe = new BookUpdateSubscribe();
    public static LitEraUpdater litera = new LitEraUpdater();

    static {
        subscribe.subscribe(new Updater());
    }

    public static void checkAllBooks() {
        Logger.getLogger().print("Проверка книг началась. На очереди " + MainParser.books.size() + " книг");
        MainParser.books.forEach(Updater::checkBook);
    }

    public static void checkBook(IBook book) {
        BookUpdateEvent event = null;
        switch (book.getType()) {
            case LITERA:
                event = litera.checkUpdateBook(book);
        }
        if (event != null) {
            Logger.getLogger().print("Обнаруженно обновление книги " + book.getNameBook() + " от " + event.updateTime);
            subscribe.sendUpdateEvent(event);
        }
    }

    @Override
    public void bookUpdate(BookUpdateEvent e) {
        String annotation = null;
        switch (e.book.getType()) {
            case LITERA:
                annotation = new LitEraParser(e.book.getUrl()).getAnnotation();
        }
        e.book.setAnnotation(annotation).setLastUpdate(e.updateTime).setSize(e.sizeUp + e.book.getSize());
    }
}
