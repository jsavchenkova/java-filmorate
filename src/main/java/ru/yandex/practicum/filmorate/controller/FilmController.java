package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.util.FilmValidate;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final GenreService genreService;

    @GetMapping
    public List<ResponseFilmDto> getfilms() {
        return filmService.getfilms();
    }

    @GetMapping("/{id}")
    public ResponseFilmDto getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public ResponseFilmDto createFilm(@Valid @RequestBody RequestFilmDto filmDto) {
        FilmValidate.textValidate(filmDto, log);
        FilmValidate.timeValidate(filmDto, log);

        return filmService.createFilm(filmDto);
    }

    @PutMapping
    public ResponseFilmDto updateFilm(@Valid @RequestBody RequestFilmDto film) {
        FilmValidate.timeValidate(film, log);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseFilmDto addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseFilmDto deleteLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<ResponseFilmDto> getPopular(@RequestParam int count) {
        return filmService.getPopular(count);
    }
}
