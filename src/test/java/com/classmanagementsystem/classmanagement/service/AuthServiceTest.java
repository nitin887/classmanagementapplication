package com.classmanagementsystem.classmanagement.service;

import com.classmanagementsystem.classmanagement.dto.LoginDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private LoginDto loginDto;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        loginDto = new LoginDto("testuser", "password");
        authentication = mock(Authentication.class);
        // Clear SecurityContextHolder before each test to ensure isolation
        SecurityContextHolder.clearContext();
    }

    @Test
    void login_Success() {
        // Given
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        );
        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);

        // When
        String result = authService.login(loginDto);

        // Then
        assertNotNull(result);
        assertEquals("User logged in successfully!", result);
        verify(authenticationManager, times(1)).authenticate(authToken);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void login_Failure_BadCredentials() {
        // Given
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        );
        when(authenticationManager.authenticate(authToken)).thenThrow(new BadCredentialsException("Bad credentials"));

        // When & Then
        assertThrows(BadCredentialsException.class, () -> authService.login(loginDto));
        verify(authenticationManager, times(1)).authenticate(authToken);
        assertNull(SecurityContextHolder.getContext().getAuthentication()); // Context should remain empty on failure
    }
}
