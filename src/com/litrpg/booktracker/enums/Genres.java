package com.litrpg.booktracker.enums;

import com.lionzxy.vkapi.util.Logger;

/**
 * com.litrpg.booktracker.enums
 * Created by LionZXY on 12.01.2016.
 * BookTracker
 */
public enum Genres {
    LitRPG("ЛитРПГ"),
    Love("Любовные романы", "Любовь","любовь", "love story", "любовный роман"),
    Detective("Детектив", "Детектив/Триллер"),
    Fantasy("Фентези", "Фэнтези"),
    Popadantsy("Попаданцы"),
    Humor("Юмор"),
    Fanfik("Фанфик", "фан", "fun"),
    ModernProse("Modern Prose", "Современная проза"),
    Fantastic("Фантастика"),
    WomensProse("Women's prose", "Женская проза"),
    TeenageFiction("Teenage fiction", "Подростковая проза"),
    Action("Боевик", "Экшн"),
    Mystery("Horror", "Мистика/Ужасы", "Мистика", "Ужасы"),
    Other("Разное"), Absurd("absurdity", "абсурд"),
    Adventure("адвенчура", "приключения"),
    Autobiographical("автобиография"),
    Biographical("биография"),
    CoolDetective("Cool Detective", "крутой детектив"),
    Counterculture("контркультура", "контркультура"),
    Crime("криминал", "преступления"),
    Cyberpunk("киберпанк", "сайберпанк"),
    Documentary("документальный"),
    Drama("dramatic", "драма"),
    Dystopia("антиутопия"),
    Epic("epic", "эпический", "эпик"),
    Erotic("sensuality", "эротика"),
    Espionage("шпионаж", "шпион"),
    Fiction("fiction", "фантастика", "фикция"),
    Gothic("готика"),
    Gunman("боевик"),
    Heroics("героика"),
    History("historic", "история", "исторический"),
    Horror("ужасы", "хоррор"),
    Irony("ирония"),
    MagicalRealism("Magical Realism", "магический реализм"),
    Maniacs("maniacs", "маньяки", "маньяки"),
    Military("военный"),
    Mysticism("мистика", "мистика"),
    Parody("spoof", "пародия"),
    Philosophical("sorcerer", "философия", "философский"),
    Police("полиция"),
    PopularScience("Popular Science", "научно-популярная"),
    Postapocalypse("postapokaliptika", "постапокалиптика", "постапокалипсис"),
    Psychological("психология"),
    Realism("реализм"),
    Religion("религия", "церковь"),
    Romance("романтика"),
    Satire("сатира"),
    ScienceFiction("Science Fiction", "научная фантастика", "НФ"),
    Sentimental("сентиментальный"),
    Social("социальный"),
    Surrealism("сюрреализм"),
    Suspense("саспенс", "ужасы", "хоррор", "мистика"),
    Thriller("триллер"),
    Tragedy("трагедия"),
    Travel("traveling", "путешествия"),
    Utopia("утопия"),
    Western("вестерн"),
    Unknown("Unknown", "Неизвестный", "Пусто", "null");

    String[] parseCod;

    Genres(String... parseCod) {
        this.parseCod = parseCod;
    }

    public static Genres getGenre(String genre) {
        for (Genres genres : Genres.values())
            if (genre.equalsIgnoreCase(genres.name()) || genre.contains(genres.name()))
                return genres;
            else
                for (String parseCod : genres.parseCod)
                    if (genre.equalsIgnoreCase(parseCod) || genre.contains(parseCod))
                        return genres;
        Logger.getLogger().print("Not found genre for " + genre);
        return Genres.Unknown;
    }
}
