package ru.yandex.practicum.filmorate.util;

import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidate {
    public static void birthdayValidate(User user, Logger log) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String errorMessage = "Дата не может быть больше текущей";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }

    public static void nameValidate(User user, Logger log) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Вместо имени использован логин.");
            user.setName(user.getLogin());
        }
    }
}
