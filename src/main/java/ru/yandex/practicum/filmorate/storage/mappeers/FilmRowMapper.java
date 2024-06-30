package ru.yandex.practicum.filmorate.storage.mappeers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashSet;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .releaseDate(rs.getDate("film_release").toLocalDate())
                .duration(Duration.ofSeconds(rs.getInt("film_duration")))
                .genres(new HashSet<>())
                .build();

        if (rs.getInt("rating_id") != 0) {
            Rating nextR = Rating.builder()
                    .id(rs.getInt("rating_id"))
                    .name(rs.getString("rating_name"))
                    .build();

            film.setRating(nextR);
        }
        while (rs.next()) {
            if (rs.getInt("genre_id") != 0) {
                Genre nextG = Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre_name"))
                        .build();

                film.getGenres().add(nextG);
            }

        }
        return film;
    }
}
