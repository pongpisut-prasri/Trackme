package manservice.trackmeh.foodtracking.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import manservice.trackmeh.foodtracking.dto.request.UserReq;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.repository.UserModelRepository;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserModelRepository userModelRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserReq req;

    @BeforeEach
    void setupReq() {
        String username = "name";
        String rawPassword = "Password";
        req = new UserReq(username, rawPassword);
    }

    @Test
    @DisplayName("createUserShouldThrowWhenUsernameAndPasswordIsNull")
    void createUserShouldThrowWhenUsernameAndPasswordIsNull() {
        req = new UserReq();
        Exception exception = assertThrows(IOException.class, () -> userServiceImpl.createUser(req));
        assertEquals("username/password is required", exception.getMessage());
    }

    @Test
    @DisplayName("createUserShouldThrowWhenUsernameIsNull")
    void createUserShouldThrowWhenUsernameIsNull() {
        req.setUsername(null);
        Exception exception = assertThrows(
                IOException.class,
                () -> userServiceImpl.createUser(req));
        assertEquals("username is required", exception.getMessage());
        // verify(userModelRepository, never()).save(any());
    }

    @Test
    @DisplayName("createUserShouldThrowWhenPasswordIsNull")
    void createUserShouldThrowWhenPasswordIsNull() {
        req.setPassword(null);
        Exception exception = assertThrows(
                IOException.class,
                () -> userServiceImpl.createUser(req));
        assertEquals("password is required", exception.getMessage());
        // verify(userModelRepository, never()).save(any());
    }

    @Test
    void createUserShouldThrowWhenUsernameExist() {
        // Arrange
        String encryptPassword = "EncryptedPassword";
        UserModel existingUser = UserModel
                .builder()
                .username(req.getUsername())
                .passwordEncrypted(encryptPassword)
                .build();

        // Stubbing the repository to return existing user when searched by username
        when(userModelRepository.findByUserName(req.getUsername())).thenReturn(existingUser);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.createUser(req));
        assertEquals("Username already exists", exception.getMessage());
        verify(userModelRepository).findByUserName(req.getUsername());
        verify(userModelRepository, never()).save(any(UserModel.class));
    }

    @Test
    void createUserHappyCase() {
        // Arrange

        String encryptPassword = "Encrypted";
        UserModel expectedData = UserModel
                .builder()
                .username(req.getUsername())
                .passwordEncrypted(encryptPassword)
                .build();
        // Stubbing

        when(userModelRepository.save(any(UserModel.class))).thenReturn(expectedData);
        when(passwordEncoder.encode(req.getPassword())).thenReturn(encryptPassword);

        // Act

        UserModel userData = userServiceImpl.createUser(req);
        // Assert
        assertAll(
                () -> assertNotNull(userData),
                () -> assertEquals(req.getUsername(), userData.getUsername()),
                () -> assertEquals(encryptPassword, userData.getPasswordEncrypted()));
        // assertDoesNotThrow(() -> userServiceImpl.createUser(req));
        verify(userModelRepository).save(any(UserModel.class));
        verify(passwordEncoder).encode(anyString());

    }

}
