package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.lionzxy.vkapi.users.User;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.user.IUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class SubscribeList extends ICommand {
    public SubscribeList() {
        super("!наЧтоПодписан");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        StringBuilder sb = new StringBuilder();
        if (user.getSubsBook().size() != 0) {
            sb.append("На данный момент Вы подписаны на следующие книги:\n");
            HashMap<TypeSite, List<IBook>> books = new HashMap<>();

            for (IBook book : user.getSubsBook()) {
                if (books.get(book.getType()) == null)
                    books.put(book.getType(), new ArrayList<>());
                books.get(book.getType()).add(book);
            }

            for (TypeSite type : books.keySet()) {
                sb.append(type.getRuss()).append(":\n");
                for (IBook book : books.get(type)) {
                    sb.append("- ").append(book.getAuthors().get(0)).append(" \"").append(book.getNameBook()).append("\"\n");
                }
                sb.append("\n");
            }
            //sb.append(book.getAuthors().get(0)).append(" \"").append(book.getNameBook()).append("\"\n");
        }
        if (user.getSubsAuthor().size() != 0) {
            sb.append("\nТакже Вы еще подписаны на ряд авторов:\n");
            for (Author author : user.getSubsAuthor())
                sb.append("- ").append(author.getName()).append("\n");
        }
        if (sb.length() == 0)
            sb.append("Хм... Вижу, вы не подписаны ни на одну из книг. Напишите \"!добавитьКнигу %ссылка%\" и сможете следить за обновлениями.");
        MessageBuffer.addMessage(new Message(sb.toString()), new User(user.getTypeID()));
    }
}
