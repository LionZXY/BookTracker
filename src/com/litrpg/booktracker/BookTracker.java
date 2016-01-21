package com.litrpg.booktracker;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.documents.VkFile;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.UsersFile;
import com.litrpg.booktracker.mysql.MySql;
import com.litrpg.booktracker.parsers.LitEraParser;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.parsers.other.ToText;

import java.io.File;
import java.util.Date;

/**
 * com.litrpg.booktracker
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class BookTracker {
    static {
        Logger.setDefaultLogger("[BookTracker]");
    }

    public static MySql DB = new MySql("book_updater", "root", "root");

    public static void main(String... args) {
        //new LitEraParser("https://lit-era.com/book/prikladnaya-nekromantiya-zapiski-mezhdu-stranic-b3904").parseBook();
        //ToText.getText(new LitEraParser("https://lit-era.com/book/parallel-kniga-chetvertaya-bog-b3228").parseBook());
        //new Message("test").addMedia("photo286477373_398516287").sendMessage(new VKUser(UsersFile.getUsers("LeaveBot.usrs")[0]),new User(3495873));
        //VKUser vk = new VKUser(UsersFile.getUsers("LeaveBot.usrs")[0]);
        //new Message("Получите и распишитесь.").addFile(, vk).sendMessage(vk, new User(3495873));
        //System.out.println(new File("book.fb2").exists());
        //new VkFile(new File("book.fb2"), new VKUser(UsersFile.getUsers("LeaveBot.usrs")[0]));
        //ToText.getAsFile(new LitEraParser("https://lit-era.com/book/rastyk-nevezuchii-b3493").parseBook());
        //System.out.println(MySql.dateToString(new Date()));
        sync();
        new LitEraParser("https://lit-era.com/book/rastyk-nevezuchii-b3493").parseBook();
    }

    static void sync() {
        MainParser.authors.addAll(DB.getAuthorsFromTable());
        MainParser.books.addAll(DB.getBooksFromTable());
    }
}
