package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users;
    Integer ids;

    public UserController() {
        users = new HashMap<>();
        ids = 1;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String errorMessage = "Электронная почта не должна быть пустой и должна содержать символ @";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            String errorMessage = "Логин не должен быть пустым";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Вместо имени использован логин.");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
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

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
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
