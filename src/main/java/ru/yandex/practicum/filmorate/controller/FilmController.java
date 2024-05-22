package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    @GetMapping
    List<Film> getfilms(){
        return new ArrayList<>();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film){
        if(film.getName().isBlank()){
            throw new ValidationException("Название не может быть пустым");
        }
        if(film.getDescription().length() > 200){
            throw new ValidationException("Слишком длинное описание. Максимальная длина 200");
        }
        if(film.getReleaseDate().isBefore(ZonedDateTime.of(1985, 11,
                20, 0,0,0, 0,
                ZoneId.of("Europe/Moscow")))){
            throw new ValidationException("Слишком старый фильм");
        }
        return film;
    }

    @PatchMapping
    public Film updateFilm (@RequestBody Film film){
        return film;
    }
}
