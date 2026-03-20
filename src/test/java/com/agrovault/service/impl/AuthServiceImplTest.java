package com.agrovault.service.impl;

import com.agrovault.dto.request.LoginRequest;
import com.agrovault.dto.request.RegisterRequest;
import com.agrovault.dto.response.AuthResponse;
import com.agrovault.entity.Role;
import com.agrovault.entity.User;
import com.agrovault.exception.BadRequestException;
import com.agrovault.repository.UserRepository;
import com.agrovault.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerCreatesUserAndReturnsToken() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Farmer One")
                .email("farmer@example.com")
                .password("secret123")
                .role("FARMER")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertThat(response.getEmail()).isEqualTo("farmer@example.com");
        assertThat(response.getRole()).isEqualTo("FARMER");
        assertThat(response.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void registerRejectsDuplicateEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Farmer One")
                .email("farmer@example.com")
                .password("secret123")
                .role("FARMER")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void loginReturnsExistingUserToken() {
        LoginRequest request = LoginRequest.builder()
                .email("owner@example.com")
                .password("owner123")
                .build();

        User user = User.builder()
                .name("Owner")
                .email(request.getEmail())
                .password("encoded-password")
                .role(Role.STORAGE_OWNER)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("login-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("login-token");
        assertThat(response.getRole()).isEqualTo("STORAGE_OWNER");
    }
}
