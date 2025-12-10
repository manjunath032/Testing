package org.example.service;

import org.example.sample.LoginResponse;
import org.example.sample.VerifyAccessResponse;
import org.springframework.stereotype.Service;

/**
 * Simple authentication/authorization service used by SOAP endpoint and REST controller.
 * Keeps the same simple logic used previously (user/pass -> dummy-token, etc.).
 */
@Service
public class AuthService {

    public LoginResponse login(String username, String password) {
        LoginResponse response = new LoginResponse();
        if ("user".equals(username) && "pass".equals(password)) {
            response.setToken("dummy-token");
            response.setStatus("SUCCESS");
            response.setMessage("Logged in");
        } else {
            response.setStatus("FAILURE");
            response.setMessage("Invalid credentials");
        }
        return response;
    }

    public VerifyAccessResponse verifyAccess(String token, String resource) {
        VerifyAccessResponse response = new VerifyAccessResponse();
        if ("dummy-token".equals(token) && "allowedResource".equals(resource)) {
            response.setAllowed(true);
            response.setReason(null);
        } else if (!"dummy-token".equals(token)) {
            response.setAllowed(false);
            response.setReason("Invalid token");
        } else {
            response.setAllowed(false);
            response.setReason("Access denied for resource");
        }
        return response;
    }
}

