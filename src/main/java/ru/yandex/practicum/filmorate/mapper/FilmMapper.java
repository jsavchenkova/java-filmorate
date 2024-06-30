package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewRequestFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static Film mapNewRequestFilmDtoToFilm(NewRequestFilmDto filmDto) {
        return Film.builder().description(filmDto.getDescription()).duration(filmDto.getDuration()).name(filmDto.getName()).releaseDate(filmDto.getReleaseDate()).genres(new HashSet<>()).build();
    }
}
