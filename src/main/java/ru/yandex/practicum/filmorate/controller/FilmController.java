package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    Integer ids = 0;
    Map<Integer, Film> films = new HashMap<>();
    @GetMapping
    List<Film> getfilms(){
        return new ArrayList<>();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film){
        if(film.getName().isBlank()){
            String messageError = "Название не может быть пустым";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if(film.getDescription().length() > 200){
            String messageError = "Слишком длинное описание. Максимальная длина 200";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if(film.getReleaseDate().isBefore(ZonedDateTime.of(1985, 11,
                20, 0,0,0, 0,
                ZoneId.of("Europe/Moscow")))){
            String messageError = "Слишком старый фильм";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        film.setId(ids);
        films.put(ids, film);
        ids++;
        log.info("Добавлен фильм");
        return film;
    }

    @PatchMapping
    public Film updateFilm (@RequestBody Film film){
        if(film.getReleaseDate().isBefore(ZonedDateTime.of(1985, 11,
                20, 0,0,0, 0,
                ZoneId.of("Europe/Moscow")))){
            String messageError = "Слишком старый фильм";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
        if(!film.getName().isBlank()){
            films.get(film.getId()).setName(film.getName());
        }
        if(!film.getDescription().isBlank()){
            films.get(film.getId()).setDescription(film.getDescription());
        }
        if(film.getReleaseDate()!=null){
            films.get(film.getId()).setReleaseDate(film.getReleaseDate());
        }
        if(film.getDuration()!=null){
            films.get(film.getId()).setDuration(film.getDuration());
        }
        log.info(String.format("Фильм id:%d изменён", film.getId()));
        return film;
    }
}
