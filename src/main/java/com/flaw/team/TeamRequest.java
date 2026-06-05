package com.flaw.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TeamRequest {
    @NotBlank
    public String name;
}
