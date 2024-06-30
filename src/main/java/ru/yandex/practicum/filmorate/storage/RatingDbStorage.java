package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;

@Repository
@RequiredArgsConstructor
public class RatingDbStorage {

    private final JdbcTemplate jdbc;
    private final RowMapper<Rating> mapper;


}
