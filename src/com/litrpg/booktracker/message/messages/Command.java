package com.litrpg.booktracker.message.messages;

import com.lionzxy.vkapi.messages.Message;
import com.litrpg.booktracker.BookTracker;

/**
 * com.litrpg.booktracker.message.messages
 * Created by LionZXY on 24.01.2016.
 * BookTracker
 */
public class Command {
    public static Message info, download = new Message("Йохохо! Ловите сокровище, капитан!").addMedia("photo286477373_399676421"),
            stop = new Message("Бот остановлен"), money, version = new Message("Версия бота: " + BookTracker.VERSION);

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("Что прикажете, Господин?\n");
        sb.append("* \"!пинг\" - Проверка доступности бота.\n");
        sb.append("* \"!добавитьКнигу %ссылка%\" - Добавить книгу в проверку на обновление.\n");
        sb.append("* \"!добавитьАвтора %ссылка%\" - Добавить автора в проверку на обновление. Можно указать ссылку на одно из его произведений\n");
        sb.append("* \"!удалитьКнигу %ссылка%\" - Удалить книгу\n");
        sb.append("* \"!удалитьАвтора %ссылка%\" - Удалить автора\n");
        //sb.append("* \"!скачать %ссылка%\" - Минимальный уровень доступа 50. Ваш - ").append(user.getPerm()).append(".\n");
        sb.append("* \"!наЧтоПодписан\" - Выводит весь список подписок.\n");
        sb.append("* \"!минимальныйРазмер %числоСимволов%\" - Назначить минимальное количество символов в обновлении. Может быть отрицательным числом.\n");
        sb.append("* \"!сказатьСпасибо %сообщение%\" - Выразить благодарность авторам бота, узнать реквезиты, а так же просто сообщить о проблеме или предложении\n");
        sb.append("Добавлять произведения в обновления можно, только если Вы состоите в одной из групп. Группы можете найти на страничке бота. Если вы являетесь участником группы, но бот не реагирует, подождите ~15 минут.\n");
        info = new Message(sb.toString()).addMedia("photo286477373_399675033");
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("Большое Вам спасибо! Ваше сообщение отправленно одному из администраторов\n");
        sb.append("Если вас это интересует, вот наши реквизиты:\n");
        sb.append("Никита Куликов (Основной пОграммист) - Qiwi: +7 (926) 659-93-36\n");
        sb.append("Нина Левковская (Всё, кроме программирования. Дизайн и развитие групп) - ЯндексДеньги: 410013444521691");
        money = new Message(sb.toString()).addMedia("photo286477373_399995250");

    }
}
