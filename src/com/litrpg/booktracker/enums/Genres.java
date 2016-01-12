package com.litrpg.booktracker.enums;

/**
 * com.litrpg.booktracker.enums
 * Created by LionZXY on 12.01.2016.
 * BookTracker
 */
public enum Genres {
    LitRPG("ЛитРПГ"),
    Love("Любовные романы", "Любовь"),
    Detective("Детектив", "Детектив/Триллер", "Триллер"),
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
    Other("Разное"),
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
        return Genres.Unknown;
    }
}
