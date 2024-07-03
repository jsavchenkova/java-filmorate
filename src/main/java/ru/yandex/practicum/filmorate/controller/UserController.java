package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.UserValidate;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Получение списка пользователей
     * @return Возвращает список пользователей
     */
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Создание пользователя
     * @param user - данные для создания нового пользователя
     * @return Возвращает созданного ползователя
     */
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        UserValidate.nameValidate(user, log);
        UserValidate.birthdayValidate(user, log);
        return userService.createUser(user);
    }

    /**
     * Обновление пользователя
     * @param user - содержит поля, которые надо обновить
     * @return Возвращает пользователя
     */
    @PutMapping
    public User updateUser(@RequestBody User user) {
        UserValidate.birthdayValidate(user, log);
        return userService.updateUser(user);
    }

    /**
     * Получение пользователя по id
     * @param id - id пользователя
     * @return Возвращает пользователя
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    /**
     * Получение друзей
     * @param id - id пользовтеля
     * @return Возвращает список пользователей (друзей)
     */
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    /**
     * Добавление друга
     * @param id - id пользователя
     * @param friendId - id друга
     * @return Возвращает пользователя, которому был добавлен друг
     */
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    /**
     * Удаление пользователя.
     * @param id - id пользователя
     * @param friendId - id друга
     * @return Возвращает пользователя, у которого был удалён друг
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.deleteFriend(id, friendId);
    }

    /**
     * Получение общих друзей
     * @param id - id  пользователя
     * @param otherId - id другого пользователя
     * @return Возвращает список общих друзей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getFriendsCommon(@PathVariable int id, @PathVariable int otherId) {
        return userService.getFriendsCommon(id, otherId);
    }
}
