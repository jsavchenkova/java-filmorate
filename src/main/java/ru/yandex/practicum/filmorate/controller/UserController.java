package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.UserValidate;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserStorage userStorage;

    @GetMapping
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        UserValidate.nameValidate(user, log);
        UserValidate.birthdayValidate(user, log);
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        UserValidate.birthdayValidate(user, log);
        return userStorage.updateUser(user);
    }
}
