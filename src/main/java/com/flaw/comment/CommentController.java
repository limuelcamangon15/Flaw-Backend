package com.flaw.comment;

import com.flaw.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest request){
        return ResponseEntity.ok(commentService.addComment(request));
    }

    @GetMapping("/bug/{bugId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByBug(@PathVariable Long bugId){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                commentService.getCommentsByBug(bugId)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Comment deleted successfully!",null) );
    }
}
