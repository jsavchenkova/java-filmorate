package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@Repository("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;

    private static final String GET_USERS_QUERY = "SELECT u.id user_id, \n" +
            "u.EMAIL user_email, \n" +
            "u.LOGIN user_login, \n" +
            "u.name user_name,\n" +
            "u.BIRTHDAY user_birthday, \n" +
            "f.ID friend_id, \n" +
            "f.EMAIL friend_email, \n" +
            "f.LOGIN friend_login, \n" +
            "f.name friend_name,\n" +
            "f.BIRTHDAY friend_birthday\n" +
            "FROM fusers u\n" +
            "LEFT JOIN USER_FRIEND uf ON u.id = uf.USER_ID\n" +
            "LEFT JOIN fusers f ON uf.FRIEND_ID = f.ID \n" +
            "ORDER BY u.ID ";
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM fusers WHERE id = ?";
    private static final String GET_USERS_BY_ID_QUERY = "SELECT * FROM fusers WHERE id in (?)";
    private static final String GET_FRIENDS_QUERY = "SELECT u.* \n" +
            "FROM fusers u\n" +
            "JOIN user_friend uf ON u.id = uf.friend_id\n" +
            "WHERE uf.user_id = ?\n ";
    private static final String CREATE_USER_QUERY = "INSERT INTO fusers(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE fusers SET email = ?, login = ?, name = ?, birthday = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO user_friend (user_id, friend_id, aproved) VALUES (?, ?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM user_friend WHERE user_id = ? and friend_id = ?";


    @Override
    public List<User> getUsers() {
        ResultSetExtractor<List<User>> extractor = new ResultSetExtractor<List<User>>() {
            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, User> users = new HashMap<>();

                while (rs.next()) {
                    int userId = rs.getInt("user_id");

                    if (!users.containsKey(userId)) {
                        User nextU = User.builder()
                                .id(userId)
                                .email(rs.getString("user_email"))
                                .login(rs.getString("user_login"))
                                .name(rs.getString("user_name"))
                                .birthday(rs.getDate("user_birthday").toLocalDate())
                                .build();
                        users.put(userId, nextU);
                    }

                    if (rs.getInt("friend_id") != 0) {
                        User.UserBuilder nextF = User.builder()
                                .id(rs.getInt("friend_id"))
                                .email(rs.getString("friend_email"))
                                .login(rs.getString("friend_login"))
                                .name(rs.getString("friend_name"));

                        if (rs.getDate("friend_birthday") != null) {
                            nextF.birthday(rs.getDate("friend_birthday").toLocalDate());
                        }

                        users.get(userId).getFriends().add(nextF.build());
                    }
                }
                return users.values().stream().toList();
            }
        };
        return jdbc.query(GET_USERS_QUERY, extractor);
    }


    @Override
    public User createUser(User user) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getEmail());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getName());
            ps.setObject(4, user.getBirthday());

            return ps;
        }, keyHolder);
        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id != null) {
            user.setId(id);
            return user;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public User updateUser(User user) {
        User oldUser = getUserById(user.getId());
        Object[] params = new Object[4];
        if (!user.getEmail().isBlank()) {
            params[0] = user.getEmail();
        } else {
            params[0] = oldUser.getEmail();
        }
        if (!user.getName().isBlank()) {
            params[2] = user.getName();
        } else {
            params[2] = oldUser.getName();
        }
        if (!user.getLogin().isBlank()) {
            params[1] = user.getLogin();
        } else {
            params[1] = oldUser.getLogin();
        }
        if (user.getBirthday() != null) {
            params[3] = user.getBirthday();
        } else {
            params[3] = oldUser.getBirthday();
        }
        int rowUpdated = jdbc.update(UPDATE_USER_QUERY, params);

        if (rowUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        try {
            User user = jdbc.queryForObject(GET_USER_BY_ID_QUERY, mapper, id);
            return user;
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException("Пользователь не найден");
        }

    }

    @Override
    public List<User> getFriends(User user) {
        return jdbc.query(GET_FRIENDS_QUERY, mapper, user.getId());
    }

    public void addFriend(User user, User friend) {
        jdbc.update(INSERT_FRIEND_QUERY, user.getId(), friend.getId(), false);
    }

    @Override
    public void removeFriend(User user, User friend) {
        jdbc.update(DELETE_FRIEND_QUERY, user.getId(), friend.getId());
    }

}
