package com.agrovault.service;

import com.agrovault.dto.request.LoginRequest;
import com.agrovault.dto.request.RegisterRequest;
import com.agrovault.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse getCurrentUser(String email);
}
