package com.example.MaslyakBank_Token.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService; // сюда Mockito внедрит mocks

    @Test
    void shouldReturnUserWhenExists() {
        // arrange
        User user = new User(1L, "Anton");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // act
        User result = userService.getById(1L);

        // assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Anton");
        verify(userRepository).findById(1L); // проверяем вызов
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
