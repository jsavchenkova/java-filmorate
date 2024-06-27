package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
 //   private final UserStorage userStorage;
    private Comparator<Film> getLikes;

    public List<Film> getfilms() {
        return filmStorage.getfilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new UserNotFoundException(String.format("Фильм с id =  %d  не найден.", film.getId()));
        }
        return filmStorage.updateFilm(film);
    }

    public Film addLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Фильм с id =  %d  не найден.", id));
        }
//        User user = userStorage.getUserById(userId);
//        if (user == null) {
//            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", id));
//        }
        film.getLikes().add(userId);
        return filmStorage.updateFilm(film);
    }

    public Film deleteLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Фильм с id =  %d  не найден.", id));
        }
//        User user = userStorage.getUserById(userId);
//        if (user == null) {
//            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", id));
//        }
        film.getLikes().remove(userId);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getPopular(int count) {
        if (count <= 0) {
            throw new ValidationException("Количество должно быть больше нуля.");
        }
        List<Film> films = filmStorage.getfilms();
        return films.stream()
                .filter(x -> x.getLikes().size() > 0)
                .sorted(Comparator.comparing(x -> x.getLikes().size()))
                .limit(count)
                .toList().reversed();
    }
}
