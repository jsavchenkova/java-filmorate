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

/**
 * Film.
 */

@Builder
@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    @DateTimeFormat(pattern = Formatters.dateFormat)
    @JsonFormat(pattern = Formatters.dateFormat)
    private LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;
}
