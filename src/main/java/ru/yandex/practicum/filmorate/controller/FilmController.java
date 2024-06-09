package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.FilmValidate;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

    @GetMapping
    public List<Film> getfilms() {
        return filmService.getfilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        FilmValidate.textValidate(film, log);
        FilmValidate.timeValidate(film, log);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        FilmValidate.timeValidate(film, log);
        return filmService.updateFilm(film);
    }
}
