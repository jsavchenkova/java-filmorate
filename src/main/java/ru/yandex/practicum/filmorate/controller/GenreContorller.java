package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreContorller {
    private final GenreService genreService;

    /**
     * Получение всех жанров
     * @return
     * Возвращает список жанров
     */
    @GetMapping
    public List<Genre> getGenres() {
        return genreService.getGenres();
    }

    /**
     * Получение жанра по id
     * @param id - id жанра
     * @return
     * Возвращает жанр
     */
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return genreService.getGenreById(id);
    }
}
