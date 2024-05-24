package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Integer ids;
    private Map<Integer, Film> films;

    public FilmController() {
        films = new HashMap<>();
        ids = 1;
    }

    @GetMapping
    public List<Film> getfilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            String messageError = "Название не может быть пустым";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            String messageError = "Слишком длинное описание. Максимальная длина 200";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String messageError = "Слишком старый фильм";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if (film.getDuration() != null && film.getDuration().isNegative()) {
            String messageError = "Длительность не может быть отрицательной";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        film.setId(ids);
        films.put(ids, film);
        ids++;
        log.info("Добавлен фильм");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String messageError = "Слишком старый фильм";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if (film.getDuration() != null && film.getDuration().isNegative()) {
            String messageError = "Длительность не может быть отрицательной";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if (!film.getName().isBlank()) {
            films.get(film.getId()).setName(film.getName());
        }
        if (!film.getDescription().isBlank()) {
            films.get(film.getId()).setDescription(film.getDescription());
        }
        if (film.getReleaseDate() != null) {
            films.get(film.getId()).setReleaseDate(film.getReleaseDate());
        }
        if (film.getDuration() != null) {
            films.get(film.getId()).setDuration(film.getDuration());
        }
        log.info(String.format("Фильм id:%d изменён", film.getId()));
        return film;
    }
}
