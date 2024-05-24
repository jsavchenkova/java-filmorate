package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.util.Formatters;

import java.time.LocalDate;

@Builder
@Data
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    @DateTimeFormat(pattern = Formatters.dateFormat)
    @JsonFormat(pattern = Formatters.dateFormat)
    private LocalDate birthday;
}
