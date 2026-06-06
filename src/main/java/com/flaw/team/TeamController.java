package com.flaw.team;

import com.flaw.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

    @PostMapping
    @PreAuthorize("hasRole('QA')")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(@Valid @RequestBody TeamRequest request){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                teamService.createTeam(request)
        ));
    }

    @PostMapping("/{teamId}/members/{userId}")
    @PreAuthorize("hasRole('QA')")
    public ResponseEntity<ApiResponse<TeamResponse>> addMember(@PathVariable Long teamId, @PathVariable Long userId){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                teamService.addMember(teamId, userId)
        ));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeam(@PathVariable Long teamId){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                teamService.getTeam(teamId)
        ));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getMyTeams(){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                teamService.getMyTeams()
        ));
    }
}
