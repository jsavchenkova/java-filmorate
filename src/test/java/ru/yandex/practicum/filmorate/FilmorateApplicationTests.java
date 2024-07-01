package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.mappeers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappeers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappeers.RatingRowMapper;
import ru.yandex.practicum.filmorate.storage.mappeers.UserRowMapper;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserStorage.class, UserDbStorage.class, UserService.class,
        FilmStorage.class, FilmDbStorage.class, FilmService.class, FilmRowMapper.class, UserRowMapper.class,
        GenreService.class, GenreRowMapper.class, RatingRowMapper.class, GenreDbStorage.class,
        RatingDbStorage.class, FilmMapper.class})
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testFindUserById() {
        User newUser = User.builder()
                .name("name")
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.now().minusYears(30))
                .build();
        newUser = userStorage.createUser(newUser);

        User user = userStorage.getUserById(newUser.getId());

        assertThat(user.getId()).isEqualTo(newUser.getId());

    }

    @Test
    public void testUpdateUser() {
        User newUser = User.builder()
                .name("name")
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.now().minusYears(30))
                .build();
        newUser = userStorage.createUser(newUser);
        User updUser = User.builder()
                .id(newUser.getId())
                .name("new name")
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.now().minusYears(30))
                .build();
        userStorage.updateUser(updUser);

        User user = userStorage.getUserById(newUser.getId());

        assertThat(user.getName()).isEqualTo("new name");
    }

    @Test
    public void testGetFilmById() {
        Film newFilm = Film.builder()
                .releaseDate(LocalDate.now().minusYears(31))
                .name("film1")
                .description("description1")
                .genres(new HashSet<>())
                .build();
        newFilm = filmStorage.createFilm(newFilm);

        Film film = filmStorage.getFilmById(newFilm.getId());

        assertThat(film.getId()).isEqualTo(newFilm.getId());
    }

    @Test
    public void testFilmUdate() {
        Film newFilm = Film.builder()
                .releaseDate(LocalDate.now().minusYears(31))
                .name("film1")
                .description("description1")
                .genres(new HashSet<>())
                .build();
        newFilm = filmStorage.createFilm(newFilm);
        newFilm.setName("newName");

        Film film = filmStorage.updateFilm(newFilm);

        assertThat(film.getName()).isEqualTo("newName");
    }
}