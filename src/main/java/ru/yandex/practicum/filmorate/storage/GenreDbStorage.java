package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage {

    private final JdbcTemplate jdbc;
    private final RowMapper<Genre> mapper;

    private static final String GET_GENRE_BY_ID_QUERY = "SELECT * FROM GENRE WHERE id = ?";
    private static final String GET_GENRE_LIST_QUERY = "SELECT * FROM GENRE";

    public Genre getGenreById(Integer id) {
        try {
            Genre genre = jdbc.queryForObject(GET_GENRE_BY_ID_QUERY, mapper, id);
            return genre;
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException("Жанр не найден");
        }
    }

    public List<Genre> getGenres() {
        return jdbc.query(GET_GENRE_LIST_QUERY, mapper);
    }
}
