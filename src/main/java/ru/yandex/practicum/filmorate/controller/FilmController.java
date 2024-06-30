package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.util.FilmValidate;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getfilms() {
        return filmService.getfilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody RequestFilmDto filmDto) {
        FilmValidate.textValidate(filmDto, log);
        FilmValidate.timeValidate(filmDto, log);
        //  FilmValidate.ratingValidate(filmDto, log);

        return filmService.createFilm(filmDto);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody RequestFilmDto film) {
        FilmValidate.timeValidate(film, log);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam int count) {
        return filmService.getPopular(count);
    }
}
