package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(int id);

    List<User> getFriends(User user);

    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);
}
