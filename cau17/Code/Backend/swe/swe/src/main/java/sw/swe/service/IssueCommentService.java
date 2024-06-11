package sw.swe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.swe.domain.Issue;
import sw.swe.domain.IssueComment;
import sw.swe.repository.IssueCommentRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class IssueCommentService {

    @Autowired
    private IssueCommentRepository commentRepository;

    /**
     * 커멘트를 DB에 저장
     */
    @Transactional
    public Long saveComment(IssueComment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    /**
     * 커멘트를 이슈에 연결
     */
    @Transactional
    public void setIssueForComment(Long issueCommentId, Issue issue) {
        IssueComment issueComment = commentRepository.findOne(issueCommentId);
        if (issueComment != null) {
            issueComment.setIssue(issue);
        } else {
            throw new IllegalArgumentException("존재하지 않는 커멘트입니다.");
        }
    }

    /**
     * 커멘트 조회
     */
    public IssueComment findOne(Long commentId) {
        return commentRepository.findOne(commentId);
    }

    /**
     * 전체 커멘트 조회
     */
    public List<IssueComment> findAllComments() {
        return commentRepository.findAll();
    }

    /**
     * 이슈 id로 조회
     */
    public List<IssueComment> findByIssueId(Long issue_id) { return commentRepository.findByIssueId(issue_id); }

    /**
     * 특정 이슈에 속한 커멘트 조회
     */
    public List<IssueComment> findAllCommentsByIssue(Long issueId) {
        return commentRepository.findByIssueId(issueId);
    }

    /**
     * 커멘트 삭제
     */
    @Transactional
    public void deleteComment(Long commentId) {
        IssueComment comment = commentRepository.findOne(commentId);
        if (comment != null) {
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("존재하지 않는 코멘트입니다.");
        }
    }
}
