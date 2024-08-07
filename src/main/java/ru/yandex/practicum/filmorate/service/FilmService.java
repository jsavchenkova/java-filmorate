package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("UserDbStorage")
    @NonNull
    private final UserStorage userStorage;

    @Autowired
    private RatingDbStorage ratingStorage;
    @Autowired
    private GenreDbStorage genreStorage;

    private Comparator<Film> getLikes;

    public List<ResponseFilmDto> getfilms() {
        List<ResponseFilmDto> response = filmStorage.getfilms().stream()
                .map(FilmMapper::mapFilmToDto)
                .toList();
        return response;
    }

    public ResponseFilmDto createFilm(RequestFilmDto filmDto) {
        Film film = FilmMapper.mapNewRequestFilmDtoToFilm(filmDto);
        try {
            if (filmDto.getMpa() != null) {
                Rating rating = ratingStorage.getRatingById(filmDto.getMpa().getId());
                film.setRating(rating);
            }
        } catch (Exception e) {
            throw new ValidationException("Неверно указан рейтинг");
        }
        try {
            if (filmDto.getGenres() != null) {
                for (GenreDto g : filmDto.getGenres()) {
                    Genre genre = genreStorage.getGenreById(g.getId());
                    film.getGenres().add(genre);
                }
            }
        } catch (Exception e) {
            throw new ValidationException("Неверно указан жанр");
        }
        return FilmMapper.mapFilmToDto(filmStorage.createFilm(film));
    }

    public ResponseFilmDto updateFilm(RequestFilmDto filmDto) {
        Film film = FilmMapper.mapNewRequestFilmDtoToFilm(filmDto);

        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new UserNotFoundException(String.format("Фильм с id =  %d  не найден.", film.getId()));
        }

        if (filmDto.getMpa() != null) {
            Rating rating = ratingStorage.getRatingById(filmDto.getMpa().getId());
            film.setRating(rating);
        }
        if (filmDto.getGenres() != null) {
            for (GenreDto g : filmDto.getGenres()) {
                Genre genre = genreStorage.getGenreById(g.getId());
                film.getGenres().add(genre);
            }
        }

        return FilmMapper.mapFilmToDto(filmStorage.updateFilm(film));
    }

    public ResponseFilmDto addLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Фильм с id =  %d  не найден.", id));
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", id));
        }
        film.getLikes().add(userId);
        return FilmMapper.mapFilmToDto(filmStorage.updateFilm(film));
    }

    public ResponseFilmDto deleteLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Фильм с id =  %d  не найден.", id));
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь с id =  %d  не найден.", id));
        }
        film.getLikes().remove(userId);
        return FilmMapper.mapFilmToDto(filmStorage.updateFilm(film));
    }

    public List<ResponseFilmDto> getPopular(int count) {
        if (count <= 0) {
            throw new ValidationException("Количество должно быть больше нуля.");
        }
        List<Film> films = filmStorage.getfilms();
        return films.stream()
                .filter(x -> x.getLikes().size() > 0)
                .sorted(Comparator.comparing(x -> x.getLikes().size()))
                .limit(count)
                .map(FilmMapper::mapFilmToDto)
                .toList().reversed();
    }

    public ResponseFilmDto getFilmById(int id) {
        return FilmMapper.mapFilmToDto(filmStorage.getFilmById(id));
    }


}
