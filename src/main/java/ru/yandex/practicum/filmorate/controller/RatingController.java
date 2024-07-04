package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    /**
     * Получение списка рейтингов
     * @return
     * Возвращает список рейтингов
     */
    @GetMapping
    public List<Rating> getRatingList() {
        return ratingService.getRatingList();
    }

    /**
     * Получение рейтинга по id
     * @param id - id рейтинга
     * @return
     * Возвращает рейтинг
     */
    @GetMapping("/{id}")
    public Rating getRatingById(@PathVariable int id) {
        return ratingService.getRatingById(id);
    }
}
