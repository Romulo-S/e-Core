package com.launchersoft.roleapi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.launchersoft.roleapi.exception.ResourceNotFoundException;
import com.launchersoft.roleapi.model.User;
import com.launchersoft.roleapi.repository.UserRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size) {

        try {
            Pageable paging = PageRequest.of(page, size);

            Page<User> users = userRepository.findAll(paging);
            return getMapResponseEntity(users);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public User createUser(@RequestBody User client) {
        return userRepository.save(client);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) {
        User user1 = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + id));

        user1.setDisplayName(user.getDisplayName());
        user1.setId(user.getId());

        userRepository.save(user1);

        return ResponseEntity.ok(user);
    }


    private ResponseEntity<Map<String, Object>> getMapResponseEntity(Page<User> userPage) {
        Map<String, Object> response = new HashMap<>();

        response.put("users", userPage.getContent());
        response.put("currentPage", userPage.getNumber());
        response.put("totalItems", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
