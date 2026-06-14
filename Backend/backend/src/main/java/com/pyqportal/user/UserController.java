package com.pyqportal.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management — ADMIN only")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}/role")
    @Operation(summary = "Promote/demote a user's role (ADMIN only)")
    public ResponseEntity<User> updateRole(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body
    ) {
        Role role = Role.valueOf(body.get("role").toUpperCase());
        return ResponseEntity.ok(userService.updateRole(id, role));
    }

    @GetMapping
    @Operation(summary = "List all users (ADMIN only)")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
}
