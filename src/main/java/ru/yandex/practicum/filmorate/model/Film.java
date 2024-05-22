package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Film.
 */
@Data
public class Film {
    private Integer id;
    private String name;
    private String Description;
    private ZonedDateTime releaseDate;
    private Duration duration;
}
