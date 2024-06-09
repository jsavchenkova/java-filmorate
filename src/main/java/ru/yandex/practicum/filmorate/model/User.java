package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.util.Formatters;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Validated
//@Builder
@Data
public class User {
    public User() {
        friends = new HashSet<>();
    }

    private Integer id;

    @Email(message = "Электронная почта должна содержать символ @")
    @NotNull(message = "Электронная почта не должна быть пустой")
    @NotBlank(message = "Электронная почта не должна быть пустой")
    private String email;

    @NotNull(message = "Логин не должен быть пустым")
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;

    private String name;

    @DateTimeFormat(pattern = Formatters.DATE_FORMAT)
    @JsonFormat(pattern = Formatters.DATE_FORMAT)
    private LocalDate birthday;

    private Set<Integer> friends;
}
