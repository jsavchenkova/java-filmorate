package ru.yandex.practicum.filmorate.util;

import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

public class FilmValidate {
    public static void timeValidate(RequestFilmDto film, Logger log) {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String messageError = "Слишком старый фильм";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if (film.getDuration() != null && film.getDuration().isNegative()) {
            String messageError = "Длительность не может быть отрицательной";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
    }

    public static void textValidate(RequestFilmDto film, Logger log) {
        if (film.getName() == null || film.getName().isBlank()) {
            String messageError = "Название не может быть пустым";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            String messageError = "Слишком длинное описание. Максимальная длина 200";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
    }

    public static void ratingValidate(RequestFilmDto film, Logger log) {
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            String messageError = "Рейтинг должен быть задан";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
    }
}
