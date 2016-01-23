package com.litrpg.booktracker;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.market.Categories;
import com.lionzxy.vkapi.market.Item;
import com.lionzxy.vkapi.market.ParseCSV;
import com.lionzxy.vkapi.market.VkMarketPhoto;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.UsersFile;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.parsers.MainParser;

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
        // System.out.println(Item.addItem(vk, 112637507, "Test", "TestTestTest", 304, 100, "http://velasat.ru/images/stories/virtuemart/product/_______________w_5624b934c5f33[1].png"));
        //Categories.getAllCategories(vk);
        new ParseCSV(new File("csvFile.csv")).addAllInMarket(vk,112637507);
        /*
        sync();
        while (!stop) {
            for (int i = 0; i < 15; i++) {
                MessageListiner.sme.checkMessage(vk);
                VKUser.sleep(1000 * 60);
            }
            Updater.checkAllBooks()
        }*/
        //new LitEraParser("https://lit-era.com/book/o-polze-neudachnoi-pomolvki-b2952").getAuthors();

    }

    static void sync() {
        MainParser.authors.addAll(DB.getAuthorsFromTable());
        MainParser.books.addAll(DB.getBooksFromTable());
        MainParser.users.addAll(DB.getUsersFromTable());
    }
}
