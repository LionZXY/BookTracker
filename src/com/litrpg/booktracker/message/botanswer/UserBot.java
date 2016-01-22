package com.litrpg.booktracker.message.botanswer;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.documents.VkFile;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.parsers.other.ToText;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class UserBot implements IAnswer {
    Message errorMsg = new Message("Простите... Кажется, что-то пошло не так :( Я не смог обработать Вашу комманду").addMedia("photo286477373_399674007");

    @Override
    public void onMessage(Message msg, VKUser vkUser) {
        if (msg.toString().startsWith("!")) {
            IUser user = MainParser.getUserByVkId(msg.getUser().getId());
            if (user == null)
                user = MainParser.addUser(new com.litrpg.booktracker.user.VKUser(msg.getUser()));
            try {
                if (msg.toString().equalsIgnoreCase("!пинг"))
                    new Message("Я тебе не какой-то бездушный робот, чтобы отзываться на твои глупые провокации!").addMedia("photo286477373_399671795").sendMessage(vkUser, user.getTypeID());
                else if (msg.toString().startsWith("!скачать")) {
                    if (user.getPerm() >= 50)
                        new Message("Йохохо! Ловите сокровище, капитан!").addMedia("photo286477373_399676421")
                                .addMedia(new VkFile(ToText.getAsFile(MainParser.getBook(msg.toString().substring(9))), vkUser).getAsVkMedia()).sendMessage(vkUser, user.getTypeID());
                    else
                        new Message("У Вас нет прав доступа на это действие.").addMedia("photo286477373_399685563").sendMessage(vkUser, user.getTypeID());
                } else if (msg.toString().startsWith("!добавитьКнигу")) {
                    user.addSub(MainParser.getBook(msg.toString().substring(15)));
                    new Message("Книга успешно добавленна к Вам в обновления. Как только она обновиться, мы дадим Вам знать!").addMedia("photo286477373_399676422").sendMessage(vkUser, user.getTypeID());
                } else if (msg.toString().equalsIgnoreCase("!наЧтоПодписан")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("На данный момент Вы подписанны на следующие книги:\n");
                    for (IBook book : user.getSubsBook())
                        sb.append(book.getAuthors().get(0)).append(" \"").append(book.getNameBook()).append("\"\n");
                    if (user.getSubsAuthor().size() != 0) {
                        sb.append("Так же Вы еще подписаны на ряд авторов:\n");
                        for (Author author : user.getSubsAuthor())
                            sb.append(author.getName()).append("\n");
                    }
                    new Message(sb.toString()).sendMessage(vkUser, user.getTypeID());
                } else if (msg.toString().equalsIgnoreCase("!инфо")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Что прикажете господин?\n");
                    sb.append("* \"!пинг\" - Проверка доступности бота.\n");
                    sb.append("* \"!добавитьКнигу %ссылка%\" - Добавить книгу в проверку на обновление.\n");
                    sb.append("* \"!скачать %ссылка%\" - Минимальный уровень доступа 50. Ваш - ").append(user.getPerm()).append(".\n");
                    sb.append("* \"!наЧтоПодписан\" - Выводит весь список подписок.\n");
                    new Message(sb.toString()).addMedia("photo286477373_399675033").sendMessage(vkUser, user.getTypeID());
                } else if (msg.toString().startsWith("!нп") && user.getTypeID() == 76844299) {
                    BookTracker.DB.updateUser(MainParser.getUserByVkId(Integer.parseInt(msg.toString().substring(4, msg.toString().indexOf(" ", 4)))).setPerm(Integer.parseInt(msg.toString().substring(msg.toString().lastIndexOf(" ") + 1))));
                } else if (msg.toString().equalsIgnoreCase("!стоп") && user.getPerm() == 100) {
                    BookTracker.stop = true;
                } else
                    new Message("Хм... Такой команды у бота нет. Ты уверен, что всё правильно ввел? Попробуй посмотреть доступные комманды \"!инфо\" ").addMedia("photo286477373_399674456").sendMessage(vkUser, user.getTypeID());
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger().print("Ошибка при обработке комманды " + msg.toString());
                errorMsg.sendMessage(vkUser, user.getTypeID());
            }
        }
    }


}
