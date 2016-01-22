package com.litrpg.booktracker.message;

import com.lionzxy.vkapi.event.IMessageListener;
import com.lionzxy.vkapi.event.NewMessageEvent;
import com.lionzxy.vkapi.event.SubscribeMessageEvent;
import com.lionzxy.vkapi.messages.Message;
import com.litrpg.booktracker.message.botanswer.IAnswer;
import com.litrpg.booktracker.message.botanswer.SimplyAnswer;
import com.litrpg.booktracker.message.botanswer.UserBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * com.litrpg.booktracker.message
 * Created by LionZXY on 08.01.2016.
 * BookTracker
 */
public class MessageListiner implements IMessageListener {
    public static SubscribeMessageEvent sme = new SubscribeMessageEvent();
    public static List<IAnswer> answers = new ArrayList<>();

    static {
        sme.addMessageListener(new MessageListiner());
        addAnswer(new SimplyAnswer("Спасибо", "Пожалуйста :)"));
        addAnswer(new UserBot());
    }

    @Override
    public void onNewMessageInPrivate(NewMessageEvent e) {
        for (IAnswer answer : answers)
            answer.onMessage(e.getMsg(), e.getVkUser());
    }

    @Override
    public void onNewMessageInMultiDialog(NewMessageEvent e) {
        if (e.getVkUser().inChat(e.getMsg().getIdChat())) {
            new Message("Данный бот работает только с личными сообщениями. " +
                    "Если вам по какой-либо причине требуется использовать данного бота в конференции, обратитесь к администрации.").setChat(e.getMsg().getIdChat()).sendMessage(e.getVkUser(), null);
            HashMap<String, String> req = new HashMap<>();
            req.put("chat_id", String.valueOf(e.getMsg().getIdChat()));
            req.put("user_id", String.valueOf(e.getVkUser().getVkID()));
            e.getVkUser().getAnswer("messages.removeChatUser", req);
        }
    }

    public static boolean addAnswer(IAnswer answer) {
        if (answers.contains(answer))
            return false;
        else {
            answers.add(answer);
            return true;
        }
    }
}
