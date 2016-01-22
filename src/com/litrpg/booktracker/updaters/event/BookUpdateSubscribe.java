package com.litrpg.booktracker.updaters.event;

import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.books.IBook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.litrpg.booktracker.updaters.event
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class BookUpdateSubscribe {
    private List<IBookUpdateListiner> subscr = new ArrayList<>();

    public void sendUpdateEvent(BookUpdateEvent bue) {
        for (IBookUpdateListiner updateListiner : subscr)
            updateListiner.bookUpdate(bue);
    }

    public void subscribe(IBookUpdateListiner bookUpdateListiner) {
        if (!subscr.contains(bookUpdateListiner))
            subscr.add(bookUpdateListiner);
        else Logger.getLogger().print("Попытка добавить слушатель событий, который уже есть в списке");
    }

    public BookUpdateEvent getBookEvent(IBook book, int sizeUp, Date updateDate) {
        return new BookUpdateEvent(this, book, sizeUp, updateDate);
    }
}

