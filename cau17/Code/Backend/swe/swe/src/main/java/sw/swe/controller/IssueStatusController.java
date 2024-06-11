package sw.swe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sw.swe.domain.IssueStatus;
import sw.swe.repository.IssueStatusRepository;
import sw.swe.service.IssueStatusService;
import sw.swe.service.UserService;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/issue-statuses")
public class IssueStatusController {

    @Autowired
    private IssueStatusService statusService;
    @Autowired
    private IssueStatusService issueStatusService;
    @Autowired
    private UserService userService;
    @Autowired
    private IssueStatusRepository issueStatusRepository;

//    @PostMapping("/create")
//    public Long createStatus(@RequestBody IssueStatus status) {
//        return statusService.saveStatus(status);
//    }
//
//    @GetMapping("/{id}")
//    public IssueStatus getStatus(@PathVariable Long id) {
//        return statusService.findOne(id);
//    }

    @GetMapping
    public List<IssueStatus> listStatuses() {
        return statusService.findAllStatuses();
    }

//    @DeleteMapping("/{id}")
//    public void deleteStatus(@PathVariable Long id) {
//        statusService.deleteStatus(id);
//    }


    /**
     * assignee 할당
     * @param assigneeRequest
     * @return
     */
    @PostMapping("/assignee")
    public boolean allocateAssignee (@RequestBody Map<String, String> assigneeRequest){
        Long issue_id = Long.parseLong(assigneeRequest.get("issue_id"));
        String assignee = assigneeRequest.get("assignee");
        String user_id = assigneeRequest.get("user_id");

        if(userService.findUserByName(assignee)!= null){
            issueStatusService.updateAssignee(assignee, issue_id);
            issueStatusService.updateStatus(issue_id, "assigned");

            return true;
        }
        else
            return false;
    }

    /**
     * assignee 추천
     * @param recommendRequest
     * @return
     */
    @PostMapping("/recommend")
    public List<String> recommendAssignee(@RequestBody Map<String, String> recommendRequest){
        Long project_id = Long.parseLong(recommendRequest.get("project_id"));

        return issueStatusService.recommendAssignee(project_id);
    }

    /**
     * status 변경
     * @param statusRequest
     * @return
     */
    @PostMapping("/update")
    public boolean changeStatus (@RequestBody Map<String, String> statusRequest){
        Long issue_id = Long.parseLong(statusRequest.get("issue_id"));
        String user_id = statusRequest.get("user_id");

        if(userService.findUserByName(user_id).get(0).getUserType().equals("PL")){
            issueStatusService.updateStatus(issue_id, "closed");
            return true;
        }
        else if(userService.findUserByName(user_id).get(0).getUserType().equals("dev")){
            issueStatusService.updateStatus(issue_id, "fixed");
            issueStatusService.updateFixed(issue_id, user_id);
            return true;
        }
        else if(userService.findUserByName(user_id).get(0).getUserType().equals("tester")){
            issueStatusService.updateStatus(issue_id, "resolved");
            return true;
        }
        else
            return false;
    }
}
