package com.flaw.comment;

import com.flaw.auth.AuthUtil;
import com.flaw.bug.Bug;
import com.flaw.bug.BugRepository;
import com.flaw.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BugRepository bugRepository;
    private final AuthUtil authUtil;

    public CommentService(CommentRepository commentRepository, BugRepository bugRepository, AuthUtil authUtil) {
        this.commentRepository = commentRepository;
        this.bugRepository = bugRepository;
        this.authUtil = authUtil;
    }

    // Add comment
    public CommentResponse addComment(CommentRequest request){
        User author = authUtil.getCurrentUser();

        Bug bug = bugRepository.findById(request.bugId)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setContent(request.content);
        comment.setBug(bug);

        // save
        commentRepository.save(comment);

        return CommentResponse.from(comment);
    }

    // Get all comments by bug id
    public List<CommentResponse> getCommentsByBug(Long bugId){
        return commentRepository.findByBugIdOrderByCreatedAtAsc(bugId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    // Delete one comment
    public void deleteComment(Long id){
        User currentUser = authUtil.getCurrentUser();

        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Comment not found"));

        if(!comment.getAuthor().getId().equals(currentUser.getId())){
            throw new RuntimeException("You can only delete your own comment!");
        }

        commentRepository.delete(comment);
    }
}
