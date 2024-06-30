package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RatingDbStorage {

    private final JdbcTemplate jdbc;
    private final RowMapper<Rating> mapper;

    private static final String GET_RATING_QUERY = "SELECT * FROM rating WHERE id = ?";
    private static final String GET_RATING_LIST_QUERY = "SELECT * FROM rating";

    public Rating getRatingById(int id) {
        try {
            Rating rating = jdbc.queryForObject(GET_RATING_QUERY, mapper, id);
            return rating;
        } catch (EmptyResultDataAccessException ex) {
            throw new RatingNotFoundException("Рейтинг указан неверно");
        }
    }

    public List<Rating> getRatingList() {
        return jdbc.query(GET_RATING_LIST_QUERY, mapper);
    }

}
