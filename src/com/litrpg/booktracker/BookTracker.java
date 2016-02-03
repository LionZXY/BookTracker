package com.litrpg.booktracker;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.auth.LoginPaswordAuth;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.UsersFile;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.SamLibUpdater;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * com.litrpg.booktracker
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class BookTracker {
    public static float VERSION = 1.3F;
    public static boolean stop = false;

    static {
        Logger.setDefaultLogger("[BookTracker]");
    }

    public static MySql DB = new MySql("book_updater", "root", "root");
    public static VKUser vk = new VKUser(new LoginPaswordAuth(UsersFile.getUsers("LeaveBot.usrs")[0],' '));

    public static void main(String... args) {
        //ToText.getAsFile(new SamLibParser("http://samlib.ru/g/gedeon/bard.shtml").parseBook());
        new SamLibUpdater(new Date());
        /*TimeZone.setDefault(TimeZone.getTimeZone("Russia/Moscow"));
        try {
            sync();
            while (!stop) {
                UserBot.fantasySubscr = vk.getUserList(98762647);
                UserBot.litrpgSubscr = vk.getUserList(48785893);
                for (int i = 0; i < 15; i++) {
                    Logger.getLogger().print("Проверка сообщений...");
                    MessageListiner.sme.checkMessage(vk);
                    MessageBuffer.flush(vk);
                    VKUser.sleep(1000 * 60);
                }
                Updater.checkAllBooks();
                Updater.checkAllAuthor();
            }
        } catch (Exception e) {
            new CrashFileHelper(e);
        }*/
    }

    static void sync() {
        MainParser.authors.addAll(DB.getAuthorsFromTable());
        MainParser.books.addAll(DB.getBooksFromTable());
        MainParser.users.addAll(DB.getUsersFromTable());
    }
}
