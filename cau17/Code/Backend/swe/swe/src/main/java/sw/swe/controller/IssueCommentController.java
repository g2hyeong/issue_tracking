package sw.swe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sw.swe.domain.Issue;
import sw.swe.domain.IssueComment;
import sw.swe.domain.IssueStatus;
import sw.swe.service.IssueCommentService;
import sw.swe.service.IssueService;
import sw.swe.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/issue-comments")
public class IssueCommentController {

    @Autowired
    private IssueCommentService commentService;
    @Autowired
    private IssueService issueService;
    @Autowired
    private UserService userService;
    @Autowired
    private IssueCommentService issueCommentService;

    /**
     * 이슈 코멘트 생성
     * @param createCommentRequest
     * @return
     */
    @PostMapping("/create")
    public boolean createIssueComment(@RequestBody Map<String, String> createCommentRequest) {
        Long issue_id = Long.parseLong(createCommentRequest.get("issue_id"));
        String content = createCommentRequest.get("content");
        String user_id = createCommentRequest.get("user_id");

        IssueComment issueComment = IssueComment.createIssueComment(
                issueService.findOne(issue_id), content, user_id);

        if (userService.findUserByName(user_id) != null) {
            issueCommentService.saveComment(issueComment);

            return true;
        }
        else
            return false;
    }

//    @GetMapping("/{id}")
//    public IssueComment getComment(@PathVariable Long id) {
//        return commentService.findOne(id);
//    }

    @PostMapping
    public List<IssueComment> listComments(@RequestBody Map<String, String> issueRequest) {
        Long issue_id = Long.parseLong(issueRequest.get("issue_id"));

        List<IssueComment> issueComments = issueCommentService.findByIssueId(issue_id);

        for (IssueComment issueComment : issueComments) {
            issueComment.setUserType(
                    userService.findUserByName(issueComment.getCommenter()).get(0).getUserType());
        }

        return commentService.findByIssueId(issue_id);
    }

//    @DeleteMapping("/{id}")
//    public void deleteComment(@PathVariable Long id) {
//        commentService.deleteComment(id);
//    }
}
