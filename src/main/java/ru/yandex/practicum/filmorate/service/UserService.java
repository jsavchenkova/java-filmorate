package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendExistsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    @Qualifier("UserDbStorage") @NonNull
    private final UserStorage userStorage;


    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", user.getId()));
        }
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        User user = userStorage.getUserById(id);
        user.setFriends(new HashSet<>(userStorage.getFriends(user)));
        return user;
    }

    ;

    public List<User> getFriends(int id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", id));
        }
        return userStorage.getFriends(user);
    }

    public User addFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", id));
        }
        User friend = userStorage.getUserById(friendId);
        if (friend == null) {
            throw new UserNotFoundException(String.format("Друг с id = %d не найден.", friendId));
        }
        List<User> currentFriendList = getFriends(user.getId());
        if (currentFriendList.contains(friend)) {
            throw new FriendExistsException("Пользователь уже есть в друзьях");
        }
        userStorage.addFriend(user, friend);
        return user;
    }

    public User deleteFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", id));
        }
        User friend = userStorage.getUserById(friendId);
        if (friend == null) {
            throw new UserNotFoundException(String.format("Друг с id = %d не найден.", friendId));
        }


        userStorage.removeFriend(user, friend);
        return user;
    }

    public List<User> getFriendsCommon(int id, int otherId) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", id));
        }
        User otherUser = userStorage.getUserById(otherId);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", otherId));
        }
        List<User> otherFriends = userStorage.getFriends(otherUser);
        return userStorage.getFriends(user).stream()
                .filter(x -> otherFriends.contains(x))
                .toList();
    }
}
