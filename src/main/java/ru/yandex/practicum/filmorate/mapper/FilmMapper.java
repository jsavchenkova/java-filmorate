package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;

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
        return ResponseFilmDto.builder()
                .id(film.getId())
                .mpa(film.getRating())
                .likes(film.getLikes())
                .description(film.getDescription())
                .duration(film.getDuration())
                .genres(film.getGenres())
                .name(film.getName())
                .releaseDate(film.getReleaseDate())
                .build();
    }
}
