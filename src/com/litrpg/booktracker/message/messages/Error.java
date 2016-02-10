package com.litrpg.booktracker.message.messages;

import com.lionzxy.vkapi.messages.Message;

/**
 * com.litrpg.booktracker.message.messages
 * Created by LionZXY on 24.01.2016.
 * BookTracker
 */
public class Error {
    public static Message notInGroup = new Message("Добавлять произведения в обновления можно, только если Вы состоите в одной из групп. Группы можете найти на страничке бота. Если вы являетесь участником группы, но бот не реагирует, подождите ~15 минут.").addMedia("photo286477373_399685563"),
            permErr = new Message("У Вас нет прав доступа на это действие.").addMedia("photo286477373_399685563"),
            withoutProc = new Message("Ссылка должна быть без знаков '%'"),
            errLink = new Message("Я не смог добавить ссылку. Отчет об ошибке отправлен админинстраторам.").addMedia("photo286477373_399685563"),
            errorMsg = new Message("Простите... Кажется, что-то пошло не так :( Я не смог обработать Вашу команду. Отчет об ошибке отправлен админинстраторам.").addMedia("photo286477373_399674007"),
            error404 = new Message("Книга по данной ссылке удалена или временно недоступна!").addMedia("photo286477373_402458528");


}
