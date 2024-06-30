package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
@Disabled
@SpringBootTest
public class UserControllerTest {
    private UserController userController;
    private User user;

    @BeforeEach
    public void init() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
        user = User.builder()
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.now().minusDays(100))
                .name("name")
                .build();
    }

    @Test
    public void getUsersTest() {
        userController.createUser(user);
        user.setId(0);

        List<User> result = userController.getUsers();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user, result.get(0));
    }

    @Test
    public void createUserTest() {
        user.setName("");

        userController.createUser(user);

        List<User> result = userController.getUsers();
        Assertions.assertEquals("login", result.get(0).getName());
    }

    @Test
    public void updateUserTest() {
        userController.createUser(user);
        user.setName("newName");
        user.setId(1);

        userController.updateUser(user);

        List<User> result = userController.getUsers();
        Assertions.assertEquals("newName", result.get(0).getName());
    }
}
