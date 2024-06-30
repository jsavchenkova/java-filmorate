package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Disabled
@SpringBootTest
public class FilmControllerTest {
    private FilmController filmController;
    private RequestFilmDto film;

    @BeforeEach
    public void init() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
        film = RequestFilmDto.builder()
                .description("Description")
                .name("Name")
                .releaseDate(LocalDate.now())
                .duration(Duration.ofMinutes(90))
                .build();
    }

    @Test
    public void getfilmsTest() {
        filmController.createFilm(film);
        film.setId(0);

        List<Film> result = filmController.getfilms();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(film, result.get(0));
    }

    @Test
    public void createFilmTest() {

        Assertions.assertThrows(ValidationException.class, () -> {
            film.setName("");
            filmController.createFilm(film);

        });
    }

//    @Test
//    public void updateFilm() {
//        filmController.createFilm(film);
//        film.setId(1);
//        film.setName("NewName");
//
//        filmController.updateFilm(film);
//
//        List<Film> result = filmController.getfilms();
//
//        Assertions.assertEquals("NewName", result.get(0).getName());
//    }

}
