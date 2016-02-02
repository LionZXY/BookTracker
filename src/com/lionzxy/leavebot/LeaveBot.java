package com.lionzxy.leavebot;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.auth.MultiAuth;
import com.lionzxy.vkapi.exceptions.VKException;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.util.ListHelper;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.UsersFile;
import com.litrpg.booktracker.mysql.MySql;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * com.lionzxy.leavebot
 * Created by LionZXY on 29.12.2015.
 * LeaveBot
 */
public class LeaveBot {
    static {
        Logger.setDefaultLogger("[VKBOT]");
    }

    public static void main(String... args) {
        String messageNew = "---\n" +
                "\n" +
                "Приветствую, любитель литрпг! \n" +
                "Рады видеть Вас в нашем уютном паблике) \n" +
                "\n" +
                "%name%, советуем в первую очередь изучить каталог. Мы старались сделать его удобным и разбили книги по категориям. \n" +
                "\n" +
                "Если испытываете трудности в поиске книги - можете написать об этом в этой теме: https://vk.com/topic-48785893_29268684?offset=1340 и мы постараемся вам помочь. А по тегу #новое@litrpg_book вы найдете самые свежие новинки жанра. \n" +
                "\n" +
                "Если знаете о ЛитРПГ, которого еще нет в нашем списке - всегда можете написать об этом, воспользовавшись кнопкой \"Предложить новость\" на стене сообщества. \n" +
                "Также с удовольствием публикуем отзывы о прочитанных книгах и рекомендации другим читателям. \n" +
                "\n" +
                "Всегда открыты для предложений и новых идей) \n" +
                "\n" +
                "-— \n" +
                "\n" +
                "С уважением, ваш классный паблик \n" +
                "https://vk.com/litrpg_book";
        String messageLeave = "Здравствуйте, %fullname%. Мы заметили, что вы вышли из группы ЛитРПГ. Нам было бы интересно узнать почему, чтобы сделать наш паблик лучше. Будем благодарны за ответ." +
                "\n\nС уважением, администрация сообщества ЛитРПГ";
        init(48785893, "vkleave", messageLeave, messageNew, 3495873, args, UsersFile.getUsers("LeaveBot.usrs"));
    }

    public static void init(int groupId, String bd, String message, String messageExit, int sendId, String[] args, String... lgnPsw) {
        Logger.getLogger().print("!!! Инициализированна новая сессия для группы #" + groupId + " !!!");
        VKUser vk = new VKUser(new MultiAuth(lgnPsw,' '));
        MySql sql = new MySql(bd, "root", "root");

        List<Integer> inBD = sql.getFullTable(ListHelper.getStringList("userid"), "vkusers").stream().map(row -> (Integer) row.get("userid")).collect(Collectors.toList());

        if (inBD.size() < 10)
            firstInit(vk, groupId, bd, args);
        Logger.getLogger().print("Локальный список пользователей получен." + inBD.size());
        List<Integer> inVK = vk.getUserList(groupId);
        Logger.getLogger().print("Текущий список пользователей получен." + inVK.size());
        List<Integer> toLeave = new ArrayList<>();
        List<Integer> toExit = new ArrayList<>();

        for (Integer user : inVK)
            if (!inBD.contains(user))
                toExit.add(user);

        for (Integer user : inBD)
            if (!inVK.contains(user))
                toLeave.add(user);

        addToTable(sql, toExit, "vkusers");
        removeFromTable(sql, toLeave, "vkusers");
        int send = 0;
        List<User> users = User.getListUser(toLeave, vk);
        List<User> usersExit = User.getListUser(toExit, vk);
        send = send + sendAllUsers(users, vk, new Message(message).addMedia("photo286477373_398516287"));
        send = send + sendAllUsers(usersExit, vk, new Message(messageExit).addMedia("photo286477373_398517605"));

        StringBuilder sb = new StringBuilder();
        sb.append("Отчет по группе #").append(groupId).append('\n').append("Вышло ").append(toLeave.size()).append(" человек").append('\n');
        sb.append("Вошло ").append(toExit.size()).append(" человек").append('\n').append("Отправленно сообщение ").append(send).append(" пользователям").append('\n').append("Вышли:\n");
        for (User user : users)
            sb.append(user.getFullName()).append(" (id").append(user.getId()).append(")\n");
        sb.append("Вошли:\n");
        for (User user : usersExit)
            sb.append(user.getFullName()).append(" (id").append(user.getId()).append(")\n");
        sb.append("Проблемы при выполнении: \n").append(VKException.errors);

        new Message(sb.toString()).sendMessage(vk, sendId);

    }

    public static int sendAllUsers(List<User> users, VKUser vk, Message message) {
        int send = 0;
        for (User user : users)
            try {
                if (message.sendMessage(vk, user))
                    send++;
            } catch (Exception e) {
                //У контакта ограничение по запросам. Не более трех в секундку.
                VKUser.sleep(1000);
                try {
                    if (message.sendMessage(vk, user))
                        send++;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        return send;
    }

    public static void firstInit(VKUser vkUser, int groupId, String bd, String[] args) {
        addToTable(new MySql(bd, "root", "root"), vkUser.getUserList(groupId), "vkusers");
    }

    public static void addToTable(MySql mySql, List<Integer> list, String table) {
        for (Integer row : list) {
            mySql.addInTable(table, ListHelper.getHashMap("userid", String.valueOf(row)));
        }
    }

    public static void removeFromTable(MySql mySql, List<Integer> list, String table) {
        for (Integer row : list) {
            mySql.removeRowFromTable("userid=" + row, table);
        }
    }
}
