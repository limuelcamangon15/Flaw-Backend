package com.flaw.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentRequest {
    @NotBlank
    public String content;

    @NotNull
    public Long bugId;
}
