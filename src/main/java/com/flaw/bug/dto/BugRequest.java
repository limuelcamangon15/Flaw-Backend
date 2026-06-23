package com.flaw.bug.dto;

import com.flaw.bug.BugCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BugRequest {
    @NotBlank
    public String title;

    @NotBlank
    public String description;

    @NotNull
    public BugCategory category;

    @NotNull
    public Long teamId;

    public Long assigneeId;
}
