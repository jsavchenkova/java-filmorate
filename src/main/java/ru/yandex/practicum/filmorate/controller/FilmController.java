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

    /**
     * Получение списка фильмов
     * @return Возвращает список
     */
    @GetMapping
    public List<ResponseFilmDto> getfilms() {
        return filmService.getfilms();
    }

    /**
     * Получение фильма по id
     * @param id - id фильма
     * @return
     * Возвращает фильм
     */
    @GetMapping("/{id}")
    public ResponseFilmDto getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    /**
     * Создание фильма
     * @param filmDto - содержит данные для создания фильма
     * @return
     * Возвращает созданный фильм
     */
    @PostMapping
    public ResponseFilmDto createFilm(@Valid @RequestBody RequestFilmDto filmDto) {
        FilmValidate.textValidate(filmDto, log);
        FilmValidate.timeValidate(filmDto, log);

        return filmService.createFilm(filmDto);
    }

    /**
     * Изменение фильма
     * @param film - содержит поля, которые надо изменить
     * @return
     * Возвращает обновлённый фильм
     */
    @PutMapping
    public ResponseFilmDto updateFilm(@Valid @RequestBody RequestFilmDto film) {
        FilmValidate.timeValidate(film, log);
        return filmService.updateFilm(film);
    }

    /**
     * Добавление лайка
     * @param id - id фильма для добавления лайка
     * @param userId - id пользователя, который ставит лайк
     * @return
     * Возвращает фильм, к которому был добавлен лайк.
     */
    @PutMapping("/{id}/like/{userId}")
    public ResponseFilmDto addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    /**
     * Удаление лайка
     * @param id - id фильма
     * @param userId - id пользователя, чей лайк необходимо удалить
     * @return
     * Возвращает фильм
     */
    @DeleteMapping("/{id}/like/{userId}")
    public ResponseFilmDto deleteLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLike(id, userId);
    }

    /**
     * Получение списка популярных фильмов
     * @param count - количество самых популярных фильмов
     * @return
     * Возвращает список фильмов
     */
    @GetMapping("/popular")
    public List<ResponseFilmDto> getPopular(@RequestParam int count) {
        return filmService.getPopular(count);
    }
}
