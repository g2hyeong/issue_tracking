package sw.swe.service;

import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import sw.swe.domain.*;
import sw.swe.repository.IssueRepository;
import sw.swe.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    // Junit 실핼할 때 스프링과 엮어서 실행
@SpringBootTest                 // 스프링부트를 띄운 상태로 테스트를 진행
@Transactional
public class IssueServiceTest {
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    IssueService issueService;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectService projectService;
    @Autowired
    private IssueCommentService issueCommentService;

    @Test
    public void testSaveIssue() {
        Issue issue = new Issue();
        issue.setTitle("Test Issue");
        issue.setDescription("This is a test issue.");

        Long issueId = issueService.saveIssue(issue);
        assertNotNull(issueId);
    }

    @Test
    public void testSetProjectForIssue() {
        Long issueId = 1L; // 연결할 이슈 ID로 대체 (현재 1L을 유지해도 됨)
        Long projectId = 1L; // 연결할 프로젝트 ID로 대체 (위와 동일)

        Project project = projectService.findOne(projectId);
        assertNotNull(project);

        issueService.setProjectForIssue(issueId, project);

        Issue issue = issueService.findOne(issueId);
        assertNotNull(issue);
        assertEquals(projectId, issue.getProject().getId());
    }

    @Test
    public void testFindIssueById() {
        Long issueId = 1L; // 존재하는 이슈 ID로 대체 (1L 유지해도 돼)
        Issue issue = issueService.findOne(issueId);
        assertNotNull(issue);
        assertEquals(issueId, issue.getId());
    }

    @Test
    public void testDeleteIssue() {
        Long issueId = 1L; // 삭제할 이슈 ID로 대체
        issueService.deleteIssue(issueId);
        assertNull(issueService.findOne(issueId));
    }
    @Test
    public void testFindAllIssues() {
        List<Issue> issues = issueService.findAllIssues();
        assertNotNull(issues);
        assertFalse(issues.isEmpty());
    }

    @Test
    public void testFindByProjectId() {
        Long projectId = 1L; // 존재하는 프로젝트 ID로 대체
        List<Issue> issues = issueService.findIssuesByProjectId(projectId);
        assertNotNull(issues);
        assertFalse(issues.isEmpty());
        for (Issue issue : issues) {
            assertEquals(projectId, issue.getProject().getId());
        }
    }

    @Test
    public void testFindIssuesByAssignee() {
        String assignee = "userB"; // 테스트 진행자의 DB 내 존재하는 assignee로 대체하세요
        List<Issue> issues = issueService.findIssuesByAssignee(assignee);
        assertNotNull(issues);
        assertFalse(issues.isEmpty());
        for (Issue issue : issues) {
            assertEquals(assignee, issue.getStatus().getAssignee());
        }
    }

    @Test
    public void testFindIssuesByStatus() {
        String status = "new"; // 테스트 진행자의 DB 내 존재하는 status로 대체
        List<Issue> issues = issueService.findIssuesByStatus(status);
        assertNotNull(issues);
        assertFalse(issues.isEmpty());
        for (Issue issue : issues) {
            assertEquals(status, issue.getStatus().getStatus());
        }
    }

    @Test
    public void testFindIssuesByReporter() {
        String reporter = "userG"; // 테스트 진행자의 DB 내 존재하는 reporter로 대체
        List<Issue> issues = issueService.findIssuesByReporter(reporter);
        assertNotNull(issues);
        assertFalse(issues.isEmpty());
        for (Issue issue : issues) {
            assertEquals(reporter, issue.getReporter());
        }
    }

    @Test
    public void testFindIssuesByTitle() {
        String title = "Project A"; // 테스트 진행자의 DB 내 존재하는 title로 대체
        List<Issue> issues = issueService.findIssuesByTitle(title);
        assertNotNull(issues);
        assertFalse(issues.isEmpty());
        for (Issue issue : issues) {
            assertTrue(issue.getTitle().toLowerCase().contains(title.toLowerCase()));
        }
    }
}