package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class UserFriend {
    private int userId;
    private int friendId;
    private boolean aproved;
}
