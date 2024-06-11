package sw.swe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sw.swe.repository.UserRepository;
import sw.swe.service.IssueCommentService;
import sw.swe.service.UserService;

import java.time.LocalDate;

@Entity
@Getter @Setter

public class IssueComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "issuecomment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    @JsonIgnore
    private Issue issue;

    @Transient
    private String userType;

    private String comment;
    private String commenter;
    private String commentedDate;

    public static IssueComment createIssueComment(Issue issue, String comment, String commenter) {
        IssueComment issueComment = new IssueComment();
        issueComment.setIssue(issue);

        issueComment.setComment(comment);
        issueComment.setCommenter(commenter);
        issueComment.setCommentedDate(String.valueOf(LocalDate.now()));

        return issueComment;
    }
}
