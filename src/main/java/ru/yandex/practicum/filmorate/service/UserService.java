package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
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
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
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
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
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
        Set<Integer> otherFriends = otherUser.getFriends();
        return user.getFriends().stream()
                .filter(x -> otherFriends.contains(x))
                .map(x -> userStorage.getUserById(x))
                .toList();
    }
}
