package ru.yandex.practicum.filmorate.storage.mappeers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingRowMapper implements RowMapper<Rating> {
    @Override
    public Rating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Rating.builder()
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .id(rs.getInt("id"))
                .build();
    }
}
