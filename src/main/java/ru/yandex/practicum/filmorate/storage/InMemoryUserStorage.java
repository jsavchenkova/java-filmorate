package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users;
    private Integer ids;

    public InMemoryUserStorage() {
        users = new HashMap<>();
        ids = 1;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {

        user.setId(ids);
        users.put(ids, user);
        ids++;
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User updateUser(User user) {

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

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public List<User> getFriends(User user) {
        return user.getFriends().stream()
                .map(x -> users.get(x))
                .toList();
    }


}
