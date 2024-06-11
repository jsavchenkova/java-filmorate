package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.util.DurationDeserializer;
import ru.yandex.practicum.filmorate.util.DurationSerializer;
import ru.yandex.practicum.filmorate.util.Formatters;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */

@Builder
@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    @DateTimeFormat(pattern = Formatters.DATE_FORMAT)
    @JsonFormat(pattern = Formatters.DATE_FORMAT)
    private LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    private Set<Integer> likes;

    public Set<Integer> getLikes() {
        if (likes == null) {
            likes = new HashSet<>();
        }
        return likes;
    }
}
