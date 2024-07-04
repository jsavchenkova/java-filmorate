package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static Film mapNewRequestFilmDtoToFilm(RequestFilmDto filmDto) {

        return Film.builder()
                .id(filmDto.getId())
                .description(filmDto.getDescription())
                .duration(filmDto.getDuration())
                .name(filmDto.getName())
                .releaseDate(filmDto.getReleaseDate())
                .genres(new HashSet<>()).build();
    }

    public static ResponseFilmDto mapFilmToDto(Film film) {
        List<Genre> genres = film.getGenres().stream()
                .sorted(Comparator.comparing(x -> x.getId()))
                .toList();
        return ResponseFilmDto.builder()
                .id(film.getId())
                .mpa(film.getRating())
                .likes(film.getLikes())
                .description(film.getDescription())
                .duration(film.getDuration())
                .genres(genres)
                .name(film.getName())
                .releaseDate(film.getReleaseDate())
                .build();
    }
}
