package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = UserDbStorage.class )
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        User newUser = User.builder()
                .name("name")
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.now().minusYears(30))
                .build();
        userStorage.createUser(newUser);
        User user = userStorage.getUserById(1);
        assertThat(user.getId()).isEqualTo(1);


    }
}