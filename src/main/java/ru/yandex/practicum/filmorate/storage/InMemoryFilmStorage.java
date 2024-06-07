package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmValidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Integer ids;
    private Map<Integer, Film> films;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
        ids = 1;
    }

    @Override
    public List<Film> getfilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {

        film.setId(ids);
        films.put(ids, film);
        ids++;
        log.info("Добавлен фильм");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {

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
