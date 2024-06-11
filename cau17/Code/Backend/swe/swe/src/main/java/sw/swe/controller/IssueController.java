package sw.swe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sw.swe.domain.Issue;
import sw.swe.domain.IssueStatus;
import sw.swe.domain.Project;
import sw.swe.domain.User;
import sw.swe.service.IssueService;
import sw.swe.service.IssueStatusService;
import sw.swe.service.ProjectService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private IssueStatusService issueStatusService;

    /**
     * 이슈 생성
     *
     * @param createIssueRequest
     * @return
     */
    @PostMapping("/create")
    public boolean createIssue(@RequestBody Map<String, String> createIssueRequest) {
        String title = createIssueRequest.get("title");
        String description = createIssueRequest.get("description");
        Long project_id = Long.parseLong(createIssueRequest.get("project_id"));
        String priority = createIssueRequest.get("priority");
        String reporter = createIssueRequest.get("reporter");

        if (projectService.findOne(project_id) != null) {
            Issue issue = Issue.createIssue
                    (projectService.findOne(project_id), title, description, reporter);

            IssueStatus issueStatus = IssueStatus.createIssueStatus
                    (issue, priority, "new", null, false, null);

            issueService.saveIssue(issue);

            issueStatusService.saveStatus(issueStatus);

            issueStatusService.setIssueForIssueStatus(issueStatus.getId(), issue);

            return true;
        }
        else
            return false;
    }

    /**
     * 유저의 id가 주어지면 그에 맞는 issueList를 출력
     * @param issueRequest
     * @return
     */
    @PostMapping
    public List<Issue> listIssues(@RequestBody Map<String, String> issueRequest) {
        String username = issueRequest.get("id");

        return issueService.findByUsername(username);
    }

    /**
     * 모든 이슈 출력
     * @return
     */
    @GetMapping
    public List<Issue> listAllIssues() {
        return issueService.findAllIssues();
    }

    /**
     * 프로젝트 id를 받아서 이슈 리스트 모두 출력
     * @param projectRequest
     * @return
     */
    @GetMapping("/projectid")
    public List<Issue> listIssuesInProject(Map<String, String> projectRequest) {
        Long project_id = Long.parseLong(projectRequest.get("project_id"));

        return issueService.findIssuesByProjectId(project_id);
    }

//    @GetMapping("/{id}")
//    public Issue getIssue(@PathVariable Long id) {
//        return issueService.findOne(id);
//    }
//
//
    @GetMapping("/project/{projectId}")
    public List<Issue> getIssuesByProjectId(@PathVariable Long projectId) {
        return issueService.findIssuesByProjectId(projectId);
    }

//    @DeleteMapping("/{id}")
//    public void deleteIssue(@PathVariable Long id) {
//        issueService.deleteIssue(id);
//    }

    /**
     * 이슈 상세정보 출력
     * @param request
     * @return
     */
    @PostMapping("/details")
    public Issue getIssueDetails(@RequestBody Map<String, String> request) {
        Long issueId = Long.parseLong(request.get("id"));

        return issueService.findOne(issueId);
    }

    @PostMapping("/search")
    public List<Issue> searchIssue(@RequestBody Map<String, String> request){
        String property = request.get("property");
        String searchWord = request.get("searchWord");

        return switch (property) {
            case "assignee" -> issueService.findIssuesByAssignee(searchWord);
            case "title" -> issueService.findIssuesByTitle(searchWord);
            case "status" -> issueService.findIssuesByStatus(searchWord);
            case "reporter" -> issueService.findIssuesByReporter(searchWord);
            default -> null;
        };

    }


    @PostMapping("/statistic")
    public Map<Integer, Long> statisticIssue(@RequestBody Map<String, String> issueRequest) {
        Long project_id = Long.parseLong(issueRequest.get("project_id"));
        String property = issueRequest.get("property");

        if(property.equals("daily")){
            return issueService.getDailyIssueCounts(project_id);
        }
        else if(property.equals("month")){
            return issueService.getMonthlyIssueCounts(project_id);
        }
        else
            return null;
    }
}
