package com.litrpg.booktracker.message.botanswer;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.documents.VkFile;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.message.MessageBuffer;
import com.litrpg.booktracker.message.messages.Command;
import com.litrpg.booktracker.message.messages.Error;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.parsers.other.ToText;
import com.litrpg.booktracker.user.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * com.litrpg.booktracker.message.botanswer
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class UserBot implements IAnswer {

    public static List<Integer> fantasySubscr = new ArrayList<>();
    public static List<Integer> litrpgSubscr = new ArrayList<>();

    @Override
    public void onMessage(Message msg, VKUser vkUser) {
        if (msg.toString().startsWith("!")) {
            IUser user = MainParser.getUserByVkId(msg.getUser().getId());
            if (user == null)
                user = MainParser.addUser(new com.litrpg.booktracker.user.VKUser(msg.getUser()));
            try {
                if (msg.toString().equalsIgnoreCase("!пинг"))
                    MessageBuffer.addMessage(new Message("Я тебе не какой-то бездушный робот, чтобы отзываться на твои глупые провокации!").addMedia("photo286477373_399671795"), new User(user.getTypeID()));
                else if (msg.toString().startsWith("!скачать")) {
                    if (user.getPerm() >= 50)
                        MessageBuffer.addMessage(Command.download
                                .addMedia(new VkFile(ToText.getAsFile(MainParser.getBook(msg.toString().substring(9))), vkUser).getAsVkMedia()), user);
                    else
                        MessageBuffer.addMessage(Error.permErr, user);
                } else if (msg.toString().startsWith("!добавитьКнигу") && TypeSite.getTypeFromUrl(msg.toString().substring(15)) != TypeSite.Unknown) {
                    if (isGroupFollower(user)) {
                        try {
                            user.addSub(MainParser.getBook(msg.toString().substring(15)));
                            MessageBuffer.addMessage(new Message("Книга \"" + MainParser.getBook(msg.toString().substring(15)).getNameBook() + "\" успешно добавлена к Вам в обновления. Как только она обновится, мы дадим Вам знать!").addMedia("photo286477373_399676422"), new User(user.getTypeID()));
                        } catch (Exception e) {
                            new CrashFileHelper(e);
                            MessageBuffer.addMessage(Error.errLink, user);
                        }
                    } else
                        MessageBuffer.addMessage(Error.notInGroup, user);
                } else if (msg.toString().equalsIgnoreCase("!наЧтоПодписан")) {
                    StringBuilder sb = new StringBuilder();
                    if (user.getSubsBook().size() != 0) {
                        sb.append("На данный момент Вы подписаны на следующие книги:\n");
                        for (IBook book : user.getSubsBook())
                            sb.append(book.getAuthors().get(0)).append(" \"").append(book.getNameBook()).append("\"\n");
                    }
                    if (user.getSubsAuthor().size() != 0) {
                        sb.append("Так же Вы еще подписаны на ряд авторов:\n");
                        for (Author author : user.getSubsAuthor())
                            sb.append(author.getName()).append("\n");
                    }
                    if (sb.length() == 0)
                        sb.append("Хм... Вижу, вы не подписаны ни на одну из книг. Напишите \"!добавитьКнигу %ссылка%\" и сможете следить за обновлениями.");
                    MessageBuffer.addMessage(new Message(sb.toString()), new User(user.getTypeID()));
                } else if (msg.toString().equalsIgnoreCase("!инфо")) {
                    MessageBuffer.addMessage(Command.info, user);
                } else if (msg.toString().startsWith("!нп") && user.getTypeID() == 76844299) {
                    BookTracker.DB.updateUser(MainParser.getUserByVkId(Integer.parseInt(msg.toString().substring(4, msg.toString().indexOf(" ", 4)))).setPerm(Integer.parseInt(msg.toString().substring(msg.toString().lastIndexOf(" ") + 1))));
                } else if (msg.toString().equalsIgnoreCase("!стоп") && user.getPerm() == 100) {
                    MessageBuffer.addMessage(Command.stop, user);
                    BookTracker.stop = true;
                } else if (msg.toString().startsWith("!добавитьКнигу %")) {
                    MessageBuffer.addMessage(Error.withoutProc, user);
                } else if (msg.toString().startsWith("!минимальныйРазмер")) {
                    int oldSize = user.getSizeUpdate();
                    user.setSizeUpdate(Integer.parseInt(msg.toString().substring(18)));
                    BookTracker.DB.updateUser(user);
                    MessageBuffer.addMessage(new Message("Теперь оповещения будут приходить вам, только если размер обновления больше " + user.getSizeUpdate() + " символов. Старый минимальный порог обновления " + oldSize + " символов."), user);
                } else if (msg.toString().startsWith("!сказатьСпасибо")) {
                    if (msg.toString().length() > 16)
                        MessageBuffer.addMessage(new Message("Сообщение от " + user.getTypeID() + ":\n" + msg.toString().substring(16)), new User(76844299));
                    MessageBuffer.addMessage(Command.money, user);
                } else if (msg.toString().startsWith("!бот версия")) {
                    MessageBuffer.addMessage(Command.version, user);
                } else {
                    MessageBuffer.addMessage(new Message("Хм... Комадны \"" + msg.toString() + "\" у бота нет. Ты уверен, что всё правильно ввел? Попробуй посмотреть доступные комманды \"!инфо\" ").addMedia("photo286477373_399674456"), new User(user.getTypeID()));
                }
            } catch (Exception e) {
                new CrashFileHelper(e);
                e.printStackTrace();
                Logger.getLogger().print("Ошибка при обработке комманды " + msg.toString());
                MessageBuffer.addMessage(Error.errorMsg, user);
            }
        }
    }

    public static boolean isGroupFollower(IUser user) {
        return litrpgSubscr.contains(user.getTypeID()) || fantasySubscr.contains(user.getTypeID());
    }


}
