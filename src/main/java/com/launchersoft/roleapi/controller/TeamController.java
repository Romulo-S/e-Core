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
import com.launchersoft.roleapi.model.Team;
import com.launchersoft.roleapi.repository.TeamRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTeams(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size) {

        try {
            Pageable paging = PageRequest.of(page, size);

            Page<Team> teams = teamRepository.findAll(paging);
            return getMapResponseEntity(teams);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public Team createTeam(@RequestBody Team team) {
        return teamRepository.save(team);
    }

    @PutMapping("{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable UUID id, @RequestBody Team teamToUpdate) {
        Team team = teamRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Team not exist with id: " + id));

        team.setTeamDescription(teamToUpdate.getTeamDescription());
        team.setTeamLead(teamToUpdate.getTeamLead());
        team.setTeamMembers(teamToUpdate.getTeamMembers());
        team.setTeamName(teamToUpdate.getTeamName());

        teamRepository.save(team);

        return ResponseEntity.ok(teamToUpdate);
    }
    private ResponseEntity<Map<String, Object>> getMapResponseEntity(Page<Team> teams) {
        Map<String, Object> response = new HashMap<>();
        response.put("teams", teams.getContent());
        response.put("currentPage", teams.getNumber());
        response.put("totalItems", teams.getTotalElements());
        response.put("totalPages", teams.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
