package com.litrpg.booktracker;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.auth.LoginPaswordAuth;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.lionzxy.vkapi.users.User;
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
import com.litrpg.booktracker.parsers.SamLibParser;
import com.litrpg.booktracker.updaters.Updater;
import com.litrpg.booktracker.user.IUser;

import java.sql.Timestamp;
import java.util.*;

/**
 * com.litrpg.booktracker
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class BookTracker {
    public static float VERSION = 1.56F;
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
        System.out.print(Timestamp.valueOf("2015-06-30 00:00:00").getTime());
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
    /*HashMap<Integer, Integer> replace = new HashMap<>();
        for (int i = 1; i < MainParser.books.size(); i++) {
            for (int j = i - 1; j > 0; j--) {
                if (MainParser.books.get(i).getUrl().equalsIgnoreCase(MainParser.books.get(j).getUrl())) {
                    DB.removeRowFromTable("id=" + MainParser.books.get(i).getIdInDB(), "books");
                    MainParser.books.remove(i);
                    replace.put(MainParser.books.get(i).getIdInDB(), MainParser.books.get(j).getIdInDB());
                    i--;
                }
            }
        }
        for (Author author : MainParser.authors) {
            for (int i = 0; i < author.books.size(); i++) {
                if (replace.get(author.books.get(i).getIdInDB()) != null) {
                    author.addBook(MainParser.getBookById(replace.get(author.books.get(i).getIdInDB())));
                    author.books.remove(author.books.get(i));
                }
            }
            DB.updateAuthorBook(author);
        }
        /*
        for (IUser usr : MainParser.users) {
            for (IBook book : usr.getSubsBook()) {
                if (replace.get(book.getIdInDB()) != null) {
                    usr.getSubsBook().remove(book);
                    usr.getSubsBook().add(MainParser.getBookById(replace.get(book.getIdInDB())));
                }
            }
            DB.updateUser(usr);
        }
    sync();
    for (IBook book : MainParser.books) {
        if (book.getUrl().startsWith("http://samlib.ru/m/mir")) {
            SamLibParser p = new SamLibParser(book.getUrl());
            book.setAuthors(p.getAuthors());
            DB.updateBook((Book) book);
        }
    }
    for (Author author : MainParser.authors) {
        for (int i = 0; i < author.books.size(); i++) {
            if (!author.books.get(i).getAuthors().contains(author)) {
                author.books.remove(author.books.get(i));
                DB.updateAuthorBook(author);
            }
        }
    }*/
}
