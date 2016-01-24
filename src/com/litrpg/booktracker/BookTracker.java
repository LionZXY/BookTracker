package com.litrpg.booktracker;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.UsersFile;
import com.litrpg.booktracker.message.MessageBuffer;
import com.litrpg.booktracker.message.MessageListiner;
import com.litrpg.booktracker.message.botanswer.UserBot;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.Updater;

/**
 * com.litrpg.booktracker
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class BookTracker {
    public static float VERSION = 1.2F;
    public static boolean stop = false;

    static {
        Logger.setDefaultLogger("[BookTracker]");
    }

    public static MySql DB = new MySql("book_updater", "root", "root");
    public static VKUser vk = new VKUser(UsersFile.getUsers("LeaveBot.usrs")[0]);

    public static void main(String... args) {

        try {
            sync();
            while (!stop) {
                UserBot.fantasySubscr = vk.getUserList(98762647);
                UserBot.litrpgSubscr = vk.getUserList(48785893);
                for (int i = 0; i < 15; i++) {
                    MessageListiner.sme.checkMessage(vk);
                    MessageBuffer.flush(vk);
                    VKUser.sleep(1000 * 60);
                }
                Updater.checkAllBooks();
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
