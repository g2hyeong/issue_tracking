package sw.swe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter

public class Issue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "issue_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "project_id")
    private Project project;

    @JsonProperty("project_id")
    public Long getProjectID() { return project != null ? project.getId() : null; }

    private String title;
    private String description;
    private String reporter;
    private String reportedDate;

    @OneToMany(mappedBy = "issue",cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<IssueComment> commentList = new ArrayList<>();

    @OneToOne(mappedBy = "issue", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JsonUnwrapped  // Json 출력시에 추가로 issuestatus 필드까지 출력
    private IssueStatus status;

    public static Issue createIssue(Project project, String title, String description, String reporter){
        Issue issue = new Issue();
        issue.setProject(project);
        /*for (IssueComment issueComment : issueComments) {
            issue.addIssueComment(issueComment);
        }*/
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setReporter(reporter);
        issue.setReportedDate(String.valueOf(LocalDate.now()));

        return issue;
    }

    public void setProject(Project project){
        this.project = project;
        project.getIssueList().add(this);
    }

    public void addIssueComment(IssueComment issueComment){
        commentList.add(issueComment);
        issueComment.setIssue(this);
    }

    public void setIssueStatus(IssueStatus issueStatus){
        this.status = issueStatus;
        issueStatus.setIssue(this);
    }
}
