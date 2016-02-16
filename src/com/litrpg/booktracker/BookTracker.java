package com.litrpg.booktracker;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.auth.LoginPaswordAuth;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.lionzxy.vkapi.util.ListHelper;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.UsersFile;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.Book;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.helper.URLHelper;
import com.litrpg.booktracker.message.MessageListiner;
import com.litrpg.booktracker.message.botanswer.UserBot;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.parsers.LitEraParser;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.Updater;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * com.litrpg.booktracker
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class BookTracker {
    public static float VERSION = 1.54F;
    public static boolean stop = false;

    static {
        Logger.setDefaultLogger("[BookTracker]");
    }

    /*
    Добавлена команда !добавить
     */
    public static MySql DB = new MySql("book_updater", "root", "root");
    public static VKUser vk = new VKUser(new LoginPaswordAuth(UsersFile.getUsers("LeaveBot.usrs")[0], ' '));

    public static void main(String... args) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        Logger.getLogger().print("Бот версии " + VERSION + " запущен! Текущее время " + new Date());
        Logger.getLogger().print("Время " + MySql.dateToString(new Date()));
        Logger.getLogger().print("Время в мс " + new Date().getTime());
        sync();
        try {
            while (!stop) {
                Updater.checkAllBooks();
                Updater.checkAllAuthor();
                UserBot.fantasySubscr = vk.getUserList(98762647);
                UserBot.litrpgSubscr = vk.getUserList(48785893);
                vk.getAnswer("account.setOnline", null);
                for (int i = 0; i < 15; i++) {
                    Logger.getLogger().debug("[" + i + "/" + 15 + "] Проверка сообщений...");
                    MessageListiner.sme.checkMessage(vk);
                    MessageBuffer.flush(vk);
                    Logger.getLogger().debug("Проверка сообщений завершена!");
                    vk.applyFriendRequest();
                    if (stop)
                        return;
                    VKUser.sleep(1000 * 60);
                }
            }

        } catch (Exception e) {
            new CrashFileHelper(e);
        }
    }

    static void sync() {
        MainParser.authors.addAll(DB.getAuthorsFromTable());
        MainParser.books.addAll(DB.getBooksFromTable());
        MainParser.users.addAll(DB.getUsersFromTable());
    }
}
