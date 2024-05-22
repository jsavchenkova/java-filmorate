package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private Date virthday;
}
