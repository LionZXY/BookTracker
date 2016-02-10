package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.litrpg.booktracker.user.IUser;

import java.util.ArrayList;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public abstract class ICommand {
    public String command;
    public static ArrayList<ICommand> commands = new ArrayList<>();

    static {
        new AddAuthor();
        new AddBook();
        new Bot();
        new Download();
        new Info();
        new Ping();
        new SetMinSize();
        new SetPermission();
        new Stop();
        new SubscribeList();
        new Thank();
        new RemoveAuthor();
        new RemoveBook();
        new Add();
    }

    public ICommand(String command) {
        this.command = command;
        commands.add(this);
    }

    public abstract void onMessage(IUser user, VKUser vkUser, Message message, String arg);

    public static boolean onMessage(IUser user, VKUser vkUser, Message message) {
        String command;
        String arg;
        if (message.toString().contains(" ")) {
            command = message.toString().substring(0, message.toString().indexOf(" "));
            arg = message.toString().substring(message.toString().indexOf(" ") + 1);
        } else {
            command = message.toString();
            arg = "";
        }
        for (ICommand iCommand : commands) {
            if (iCommand.command.equalsIgnoreCase(command)) {
                iCommand.onMessage(user, vkUser, message, arg);
                return true;
            }
        }
        return false;
    }

}
