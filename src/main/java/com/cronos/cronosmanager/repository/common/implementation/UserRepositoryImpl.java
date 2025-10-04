package com.cronos.cronosmanager.repository.common.implementation;

import com.cronos.cronosmanager.exception.common.ValidationException;
import com.cronos.cronosmanager.model.common.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.cronos.cronosmanager.query.UserQuery.*;
import static java.lang.String.format;
import static java.sql.Types.VARCHAR;
import static java.util.Map.of;
import com.cronos.cronosmanager.repository.common.UserRepository;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcClient jdbc;

    @Override
    public User getUserByUuid(String uuid) {
        try {
            return jdbc.sql(SELECT_USER_BY_USER_ID_QUERY).param("userUuid", uuid).query(User.class).single();
        } catch (EmptyResultDataAccessException exception) {
            throw new ValidationException(format("No user found by UUID %s", uuid));
        } catch (Exception exception) {
            throw new ValidationException("An error occurred. Please try again.");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return jdbc.sql(SELECT_USER_BY_EMAIL_QUERY).param("email", email).query(User.class).single();
        } catch (EmptyResultDataAccessException exception) {
            throw new ValidationException(format("No user found by email %s", email));
        } catch (Exception exception) {
            throw new ValidationException("An error occurred. Please try again.");
        }
    }

    public User getUserById(Long id) {
        try {
            return jdbc.sql(SELECT_USER_BY_ID_QUERY).param("id", id).query(User.class).single();
        } catch (EmptyResultDataAccessException exception) {
            throw new ValidationException(format("No user found by ID %s", id));
        } catch (Exception exception) {
            throw new ValidationException("An error occurred. Please try again.");
        }
    }

    public boolean validateCode(String userId, String code) {
        return true;
    }

    @Override
    public void resetLoginAttempts(String userUuid) {
        try {
            jdbc.sql(RESET_LOGIN_ATTEMPTS_QUERY).param("userUuid", userUuid).update();
        } catch (EmptyResultDataAccessException exception) {
            throw new ValidationException(format("No user found by ID %s", userUuid));
        } catch (Exception exception) {
            throw new ValidationException("An error occurred. Please try again.");
        }
    }

    @Override
    public void addLoginDevice(Long userId, String device, String client, String ipAddress) {
        try {
            jdbc.sql(INSERT_NEW_DEVICE_QUERY).params(of("userId", userId, "device", device, "client", client, "ipAddress", ipAddress)).update();
        } catch (EmptyResultDataAccessException exception) {
            throw new ValidationException(format("No user found by ID %s", userId));
        } catch (Exception exception) {
            throw new ValidationException("An error occurred. Please try again.");
        }
    }

    @Override
    public void setLastLogin(Long userId) {
        try {
            jdbc.sql(SET_LAST_LOGIN_QUERY).param("userId", userId).update();
        } catch (EmptyResultDataAccessException exception) {
            throw new ValidationException(format("No user found by ID %s", userId));
        } catch (Exception exception) {
            throw new ValidationException("An error occurred. Please try again.");
        }
    }

    @Override
    public void updateLoginAttempts(String email) {
        try {
            jdbc.sql(UPDATE_LOGIN_ATTEMPTS).param("email", email).update();
        } catch (EmptyResultDataAccessException exception) {
            throw new ValidationException(format("No user found by email %s", email));
        } catch (Exception exception) {
            throw new ValidationException("An error occurred. Please try again.");
        }
    }

    private SqlParameterSource getParamSource(String firstName, String lastName, String email, String password, String verificationKey) {
        return new MapSqlParameterSource()
                .addValue("userId", UUID.randomUUID().toString(), VARCHAR)
                .addValue("firstName", firstName, VARCHAR)
                .addValue("lastName", lastName, VARCHAR)
                .addValue("email", email.trim().toLowerCase(), VARCHAR)
                .addValue("password", password, VARCHAR)
                .addValue("verificationKey", verificationKey, VARCHAR)
                .addValue("credentialId", UUID.randomUUID().toString(), VARCHAR);
    }

    private SqlParameterSource getParamSource(String userId, String firstName, String lastName, String email, String phone, String bio) {
        return new MapSqlParameterSource()
                .addValue("userId", userId, VARCHAR)
                .addValue("firstName", firstName, VARCHAR)
                .addValue("lastName", lastName, VARCHAR)
                .addValue("email", email.trim().toLowerCase(), VARCHAR)
                .addValue("email", email, VARCHAR)
                .addValue("phone", phone, VARCHAR)
                .addValue("bio", bio, VARCHAR);
    }
}
