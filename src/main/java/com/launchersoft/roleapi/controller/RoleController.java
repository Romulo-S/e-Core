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
import com.launchersoft.roleapi.model.Roles;
import com.launchersoft.roleapi.model.User;
import com.launchersoft.roleapi.repository.RoleRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRoles(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size) {

        try {
            Pageable paging = PageRequest.of(page, size);

            Page<Roles> users = roleRepository.findAll(paging);
            return getMapResponseEntity(users);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public Roles createRole(@RequestBody Roles role) {
        return roleRepository.save(role);
    }

    @PutMapping("{id}")
    public ResponseEntity<Roles> updateRole(@PathVariable UUID id, @RequestBody Roles role) {
        Roles role1 = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role not exist with id :" + id));

        role1.setRoleName(role.getRoleName());
        role1.setRoleDescription(role.getRoleDescription());

        Roles updatedRole = roleRepository.save(role1);
        return ResponseEntity.ok(updatedRole);
    }
    private ResponseEntity<Map<String, Object>> getMapResponseEntity(Page<Roles> userPage) {
        Map<String, Object> response = new HashMap<>();

        response.put("users", userPage.getContent());
        response.put("currentPage", userPage.getNumber());
        response.put("totalItems", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
