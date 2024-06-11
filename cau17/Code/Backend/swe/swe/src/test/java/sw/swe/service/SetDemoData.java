package sw.swe.service;

import jakarta.transaction.Transactional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import sw.swe.domain.Project;
import sw.swe.domain.User;
import sw.swe.repository.IssueRepository;
import sw.swe.repository.ProjectRepository;
import sw.swe.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    // Junit 실핼할 때 스프링과 엮어서 실행
@SpringBootTest                 // 스프링부트를 띄운 상태로 테스트를 진행
@Transactional                  // 실제로 DB에 insert 문을 삽입하지 않고 Rollback함. 테스트를 용이하게 해준다.
public class SetDemoData {
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Rollback(false)
    @Test
    public void createDemoData(){
        Project project = new Project();
        project.setTitle("Demo Project");
        project.setDescription("This is a project for demo.");

        Long projectId = projectService.saveProject(project);

        String[] PL = {"PL1", "PL2"};
        String[] dev = {"dev1", "dev2", "dev3", "dev4", "dev5",
                "dev6", "dev7", "dev8", "dev9", "dev10"};
        String[] tester = {"tester1", "tester2", "tester3", "tester4", "tester5"};


        for (String username : PL){
            User user = new User();
            user.setProject(project);
            user.setUserType("PL");
            user.setUserName(username);
            user.setUserPW(username);
            userService.saveUser(user);
        }
        for (String username : dev){
            User user = new User();
            user.setProject(project);
            user.setUserType("dev");
            user.setUserName(username);
            user.setUserPW(username);
            userService.saveUser(user);
        }
        for (String username : tester) {
            User user = new User();
            user.setProject(project);
            user.setUserType("tester");
            user.setUserName(username);
            user.setUserPW(username);
            userService.saveUser(user);
        }
    }

}