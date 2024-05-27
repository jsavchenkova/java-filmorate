package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public User createUser(@Valid @RequestBody User user) {
        UserValidate.nameValidate(user, log);
        UserValidate.birthdayValidate(user, log);
        user.setId(ids);
        users.put(ids, user);
        ids++;
        log.info("Пользователь добавлен");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        UserValidate.birthdayValidate(user, log);
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
