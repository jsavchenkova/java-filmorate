package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping
    List<User> getfUsers(){
        return new ArrayList<>();
    }

    @PostMapping
    public User createUser(@RequestBody User user){

        return user;
    }

    @PatchMapping
    public User updateUser (@RequestBody User user){
        return user;
    }
}
