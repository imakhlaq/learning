package com.akhlaq.securenote.controllers;

import com.akhlaq.securenote.models.User;
import com.akhlaq.securenote.services.impl.UserServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;

    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PutMapping("/update-role")
    public ResponseEntity<String> updateRole(@RequestParam Long userId,
                                             @RequestParam String roleName) {
        userService.updateUsers(userId, roleName);
        return new ResponseEntity<>("Role updated", HttpStatus.OK);
    }

}