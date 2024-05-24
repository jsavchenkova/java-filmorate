package ru.yandex.practicum.filmorate.util;

import java.time.format.DateTimeFormatter;

public class Formatters {
    public static final String dateTimeFormat = "yyyy.MM.dd HH:mm VV";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);

    public static final String dateFormat = "yyyy-MM-dd";
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
}
