package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mappeers.FilmRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

@Primary
@Repository("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbc;
    private final FilmRowMapper mapper;

    private static final String INSERT_FILM_QUERY = "INSERT INTO film (name, description, release, duration, " +
            "rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO film_genre (film_id, genre_id) " +
            "VALUES (?,?)";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
    private static final String GET_FILMS_QUERY = "SELECT f.ID film_id,\n" +
            "f.NAME film_name,\n" +
            "f.DESCRIPTION film_description,\n" +
            "f.\"RELEASE\" film_release,\n" +
            "f.DURATION film_duration,\n" +
            "r.ID rating_id,\n" +
            "r.NAME  rating_name,\n" +
            "r.DESCRIPTION rating_description,\n" +
            "g.ID genre_id,\n" +
            "g.NAME genre_name\n" +
            "FROM FILM f \n" +
            "LEFT JOIN RATING r ON f.RATING_ID = r.ID \n" +
            "LEFT JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID \n" +
            "LEFT JOIN GENRE g ON fg.GENRE_ID = g.ID ";

    private static final String GET_FILM_BY_ID_QUERY = "SELECT f.ID film_id,\n" +
            "f.NAME film_name,\n" +
            "f.DESCRIPTION film_description,\n" +
            "f.\"RELEASE\" film_release,\n" +
            "f.DURATION film_duration,\n" +
            "r.ID rating_id,\n" +
            "r.NAME  rating_name,\n" +
            "r.DESCRIPTION rating_description,\n" +
            "g.ID genre_id,\n" +
            "g.NAME genre_name\n" +
            "FROM FILM f \n" +
            "LEFT JOIN RATING r ON f.RATING_ID = r.ID \n" +
            "LEFT JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID \n" +
            "LEFT JOIN GENRE g ON fg.GENRE_ID = g.ID " +
            "WHERE f.id = ? ";


    private static final String GET_LIKES_QUERY = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String UPDATE_FILM_QUERY = "UPDATE film SET " +
            "name = ?, " +
            "description = ?," +
            "release =?, " +
            "duration = ?," +
            "rating_id =? ";

    private static final String DELETE_GENRES_QUERY = "DELETE from film_genre WHERE film_id = ?";
    private static final String DELETE_LIKE_QUERY = "DELETE from likes WHERE film_id = ?";


    @Override
    public List<Film> getfilms() {
        ResultSetExtractor<List<Film>> extractor = new ResultSetExtractor<List<Film>>() {
            @Override
            public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, Film> films = new HashMap<>();

                while (rs.next()) {
                    int filmId = rs.getInt("film_id");

                    if (!films.containsKey(filmId)) {
                        Film nextF = Film.builder()
                                .id(filmId)
                                .name(rs.getString("film_name"))
                                .description(rs.getString("film_description"))
                                .releaseDate(rs.getDate("film_release").toLocalDate())
                                .duration(Duration.ofSeconds(rs.getInt("film_duration")))
                                .genres(new HashSet<>())
                                .build();
                        films.put(filmId, nextF);
                    }

                    if (rs.getInt("rating_id") != 0) {
                        Rating nextR = Rating.builder()
                                .id(rs.getInt("rating_id"))
                                .name(rs.getString("rating_name"))
                                .build();

                        films.get(filmId).setRating(nextR);

                    }

                    if (rs.getInt("genre_id") != 0) {
                        Genre nextG = Genre.builder()
                                .id(rs.getInt("genre_id"))
                                .name(rs.getString("genre_name"))
                                .build();

                        films.get(filmId).getGenres().add(nextG);
                    }
                }
                for (Integer i : films.keySet()) {
                    Set<Integer> likes = new HashSet<>(getLikes(i));
                    films.get(i).setLikes(likes);
                }
                return films.values().stream().toList();
            }
        };

        return jdbc.query(GET_FILMS_QUERY, extractor);
    }

    @Override
    public Film createFilm(Film film) {

        Integer ratingId;
        if (film.getRating() != null) {
            ratingId = film.getRating().getId();
        } else {
            ratingId = null;
        }
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_FILM_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, ratingId);

            return ps;
        }, keyHolder);
        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id != null) {
            film.setId(id);
            for (Genre g : film.getGenres()) {
                jdbc.update(INSERT_FILM_GENRE_QUERY, id, g.getId());
            }

            return film;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        Film oldFilm = getFilmById(film.getId());
        Object[] params = new Object[5];
        if (film.getName() != null && !film.getName().isBlank()) {
            params[0] = film.getName();
        } else {
            params[0] = oldFilm.getName();
        }
        if (film.getDescription() != null && !film.getDescription().isBlank()) {
            params[1] = film.getDescription();
        } else {
            params[1] = oldFilm.getDescription();
        }
        if (film.getReleaseDate() != null) {
            params[2] = film.getReleaseDate();
        } else {
            params[2] = oldFilm.getReleaseDate();
        }
        if (film.getDuration() != null) {
            params[3] = film.getDuration();
        } else {
            params[3] = oldFilm.getDuration();
        }
        if (film.getRating() != null) {
            params[4] = film.getRating().getId();
        } else {
            if (oldFilm.getRating() != null) {
                params[4] = oldFilm.getRating().getId();
            }
        }
        int rowUpdated = jdbc.update(UPDATE_FILM_QUERY, params);

        if (rowUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            //    jdbc.update(DELETE_GENRES_QUERY, film.getId());
            for (Genre g : film.getGenres()) {
                jdbc.update(INSERT_FILM_GENRE_QUERY, film.getId(), g.getId());
            }
        }
        jdbc.update(DELETE_LIKE_QUERY, film.getId());
        if (film.getLikes() != null && film.getLikes().size() > 0) {

            for (Integer l : film.getLikes()) {
                jdbc.update(INSERT_LIKE_QUERY, film.getId(), l);
            }
        }

        return film;
    }

    @Override
    public Film getFilmById(int id) {
        try {
            Film film = jdbc.queryForObject(GET_FILM_BY_ID_QUERY, mapper, id);
            Set<Integer> likes = new HashSet<>(getLikes(id));
            film.setLikes(likes);
            return film;
        } catch (EmptyResultDataAccessException ex) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    private List<Integer> getLikes(Integer film_id) {
        return jdbc.queryForList(GET_LIKES_QUERY, Integer.class, film_id);
    }


}
