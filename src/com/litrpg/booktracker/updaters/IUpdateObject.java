package com.litrpg.booktracker.updaters;

import java.util.Date;

/**
 * com.litrpg.booktracker.updaters
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public interface IUpdateObject {

    Date getLastCheck();
    IUpdateObject setLastCheck(Date date);

    Date getLastUpdate();
    IUpdateObject setLastUpdate(Date date);
}
