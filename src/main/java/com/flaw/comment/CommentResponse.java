package com.flaw.comment;

import java.time.LocalDateTime;

public class CommentResponse {
    public Long id;
    public String content;
    public String authorName;
    public Long bugId;
    public LocalDateTime createdAt;

    public static CommentResponse from(Comment comment){
        CommentResponse res = new CommentResponse();
        res.id = comment.getId();
        res.content = comment.getContent();
        res.authorName = comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName();
        res.bugId = comment.getBug().getId();
        res.createdAt = comment.getCreatedAt();

        return res;
    }
}
