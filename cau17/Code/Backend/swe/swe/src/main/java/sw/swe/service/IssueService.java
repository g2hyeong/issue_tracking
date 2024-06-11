package sw.swe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.swe.domain.Issue;
import sw.swe.domain.IssueStatus;
import sw.swe.domain.Project;
import sw.swe.repository.IssueRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueStatusService issueStatusService;
    @Autowired
    private UserService userService;

    /**
     * 이슈를 DB에 저장
     */
    @Transactional
    public Long saveIssue(Issue issue) {
        issueRepository.save(issue);
        return issue.getId();
    }

    /**
     * 해당 이슈와 소속된 프로젝트 값을 연동 (이슈의 프로젝트 외부 키에 프로젝트 키를 저장)
     */
    @Transactional
    public void setProjectForIssue(Long issueId, Project project) {
        Issue issue = issueRepository.findOne(issueId);
        if (issue != null) {
            issue.setProject(project);
        } else {
            throw new IllegalArgumentException("존재하지 않는 이슈입니다.");
        }
    }

    /**
     * 이슈 조회
     */
    public Issue findOne(Long issueId) {
        return issueRepository.findOne(issueId);
    }

    /**
     * 전체 이슈 조회
     */
    public List<Issue> findAllIssues() {
        return issueRepository.findAll();
    }

    /**
     * 유저 이름에 대한 이슈 조회
     */
    public List<Issue> findByUsername(String username) {
        List<Issue> issueList = new ArrayList<>();
        String usertype = null;
        if(username.equals("admin")) {
            usertype = "admin";
        }
        else {
            System.out.println(username);
            System.out.println(userService.findUserByName(username));
            usertype = userService.findUserByName(username).get(0).getUserType();
        }

        List<IssueStatus> issueStatuses = new ArrayList<>();
        if(usertype.equals("dev")) {
            // findByAssignee로 배정된 이슈를 가져옴
            List<IssueStatus> assignedIssues = issueStatusService.findByAssignee(username);
            // findByStatus로 "fixed"가 아닌 이슈를 가져옴
            List<IssueStatus> notFixedIssues = issueStatusService.findByStatus("fixed");

            // assignedIssues에서 notFixedIssues를 제외한 이슈를 필터링
            issueStatuses = assignedIssues.stream()
                    .filter(issue -> !notFixedIssues.contains(issue))
                    .collect(Collectors.toList());
        }
        else if(usertype.equals("tester")) {
            issueStatuses = issueStatusService.findByStatus("fixed");
        }
        else if (usertype.equals("PL")) {
            issueStatuses = issueStatusService.findByStatus("new");
            issueStatuses.addAll(issueStatusService.findByStatus("resolved"));
        }


        if(issueStatuses != null) {
            for (IssueStatus issueStatus : issueStatuses) {
                issueList.add(findOne(issueStatus.getIssue().getId()));
            }
        }

        return issueList;
    }

    public List<Issue> findIssuesByAssignee(String assignee) {
        return issueRepository.findByAssignee(assignee);
    }

    public List<Issue> findIssuesByStatus(String status) {
        return issueRepository.findByStatus(status);
    }

    public List<Issue> findIssuesByReporter(String reporter) {
        return issueRepository.findByReporter(reporter);
    }

    public List<Issue> findIssuesByTitle(String title) {
        return issueRepository.findByTitle(title);
    }

    /**
     * 특정 프로젝트의 모든 이슈 조회
     */
    public List<Issue> findIssuesByProjectId(Long projectId) {
        return issueRepository.findByProjectId(projectId);
    }

    /**
     * 이슈 삭제
     */

    @Transactional
    public void deleteIssue(Long issueId) {
        Issue issue = issueRepository.findOne(issueId);
        if (issue != null) {
            issueRepository.delete(issue);
        } else {
            throw new IllegalArgumentException("존재하지 않는 이슈입니다.");
        }
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Map<Integer, Long> getMonthlyIssueCounts(Long project_id) {
        List<Issue> issues = issueRepository.findByProjectId(project_id);
        return issues.stream()
                .collect(Collectors.groupingBy(
                        issue -> LocalDate.parse(issue.getReportedDate(), formatter).getMonthValue(),
                        Collectors.counting()
                ));
    }

    // Method to get issues count by day
    public Map<Integer, Long> getDailyIssueCounts(Long project_id) {
        List<Issue> issues = issueRepository.findByProjectId(project_id);
        return issues.stream()
                .collect(Collectors.groupingBy(
                        issue -> LocalDate.parse(issue.getReportedDate(), formatter).getDayOfMonth(),
                        Collectors.counting()
                ));
    }

}
