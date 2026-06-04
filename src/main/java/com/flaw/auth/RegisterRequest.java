package com.flaw.auth;

import com.flaw.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterRequest {
    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    @Email
    @NotBlank
    public String email;

    @NotBlank
    public String password;

    @NotNull
    public Role role;
}