package com.litrpg.booktracker;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.market.Categories;
import com.lionzxy.vkapi.market.Item;
import com.lionzxy.vkapi.market.ParseCSV;
import com.lionzxy.vkapi.market.VkMarketPhoto;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.UsersFile;
import com.litrpg.booktracker.message.MessageListiner;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.parsers.LitEraParser;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.parsers.other.ToText;
import com.litrpg.booktracker.updaters.Updater;

import java.io.File;

/**
 * com.litrpg.booktracker
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class BookTracker {
    public static float VERSION = 1.1F;
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
                for (int i = 0; i < 15; i++) {
                    MessageListiner.sme.checkMessage(vk);
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
