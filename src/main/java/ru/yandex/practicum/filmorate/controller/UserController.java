package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    Integer ids = 0;

    @GetMapping
    List<User> getfUsers() {
        return new ArrayList<>();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String errorMessage = "Электронная почта не должна быть пустой и должна содержать символ @";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (user.getLogin().isBlank()) {
            String errorMessage = "Логин не должен быть пустым";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (user.getName().isBlank()) {
            log.info("Вместо имени использован логин.");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().after(new Date())) {
            String errorMessage = "Дата не может быть больше текущей";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        user.setId(ids);
        users.put(ids, user);
        ids++;
        log.info("Пользователь добавлен");
        return user;
    }

    @PatchMapping
    public User updateUser(@RequestBody User user) {
        if (user.getBirthday().after(new Date())) {
            String errorMessage = "Дата не может быть больше текущей ";
            log.error(errorMessage);
        }
        if (!user.getEmail().isBlank()) {
            users.get(user.getId()).setEmail(user.getEmail());
        }
        if (!user.getName().isBlank()) {
            users.get(user.getId()).setName(user.getName());
        }
        if (!user.getLogin().isBlank()) {
            users.get(user.getId()).setLogin(user.getLogin());
        }
        if (user.getBirthday() != null) {
            users.get(user.getId()).setBirthday(user.getBirthday());
        }
        log.info(String.format("Пользователь id:%d изменён", user.getId()));
        return user;
    }
}
