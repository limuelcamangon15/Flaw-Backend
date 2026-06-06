package com.flaw.bug;

import com.flaw.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs/")
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @PostMapping
    public ResponseEntity<BugResponse> createBug(@Valid @RequestBody BugRequest request){
        return ResponseEntity.ok(bugService.createBug(request));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<ApiResponse<List<BugResponse>>> getBugsByTeam(
            @PathVariable Long teamId,
            @RequestParam(required = false) BugStatus status,
            @RequestParam(required = false) BugCategory category){

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                bugService.getBugsByTeam(teamId, status, category)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BugResponse>> getBugById(@PathVariable Long id){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                bugService.getBugById(id)
        ));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BugResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam  BugStatus status){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                bugService.updateStatus(id, status)
        ));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<BugResponse>> assignBug(
            @PathVariable Long id,
            @RequestParam Long userId){
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "success",
                        bugService.assignBug(id, userId)
                ));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BugResponse>>> getMyBugs(){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                bugService.getMyBugs()
        ));
    }
}
