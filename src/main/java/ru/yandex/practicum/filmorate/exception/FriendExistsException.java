package ru.yandex.practicum.filmorate.exception;

public class FriendExistsException extends RuntimeException {
    public FriendExistsException(String message) {
        super(message);
    }
}
