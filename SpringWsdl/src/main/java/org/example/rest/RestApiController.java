package org.example.rest;

import org.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Simple REST API to mirror the SOAP operations for easy testing from REST clients.
 * Endpoints:
 *  - POST /api/sample    { "name": "..." } -> { "message": "...", "status": "..." }
 *  - POST /api/login     { "username": "...", "password": "..." } -> { "token": "...", "status": "...", "message": "..." }
 *  - POST /api/verify    { "token": "...", "resource": "..." } -> { "allowed": true|false, "reason": "..." }
 */
@RestController
@RequestMapping("/api")
public class RestApiController {

    private final AuthService authService;

    public RestApiController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sample")
    public ResponseEntity<SampleResponseDto> sample(@RequestBody SampleRequestDto req) {
        SampleResponseDto resp = new SampleResponseDto();
        resp.setMessage("Hello, " + (req.getName() == null ? "" : req.getName()) + "!");
        resp.setStatus("SUCCESS");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto req) {
        var svcResp = authService.login(req.getUsername(), req.getPassword());
        LoginResponseDto dto = new LoginResponseDto();
        dto.setToken(svcResp.getToken());
        dto.setStatus(svcResp.getStatus());
        dto.setMessage(svcResp.getMessage());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponseDto> verify(@RequestBody VerifyRequestDto req) {
        var svcResp = authService.verifyAccess(req.getToken(), req.getResource());
        VerifyResponseDto dto = new VerifyResponseDto();
        dto.setAllowed(Boolean.TRUE.equals(svcResp.isAllowed()));
        dto.setReason(svcResp.getReason());
        return ResponseEntity.ok(dto);
    }

    // DTOs used by the REST API
    public static class SampleRequestDto {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class SampleResponseDto {
        private String message;
        private String status;
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class LoginRequestDto {
        private String username;
        private String password;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponseDto {
        private String token;
        private String status;
        private String message;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class VerifyRequestDto {
        private String token;
        private String resource;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getResource() { return resource; }
        public void setResource(String resource) { this.resource = resource; }
    }

    public static class VerifyResponseDto {
        private boolean allowed;
        private String reason;
        public boolean isAllowed() { return allowed; }
        public void setAllowed(boolean allowed) { this.allowed = allowed; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}

