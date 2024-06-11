package sw.swe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sw.swe.domain.*;
import sw.swe.service.ProjectService;
import sw.swe.service.UserService;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    /**
     * 계정 생성
     * @param createUserRequest
     * @return
     */
    @PostMapping("/create")
    public boolean createUser(@RequestBody Map<String, String> createUserRequest) {
        String username = createUserRequest.get("id");
        String password = createUserRequest.get("password");
        String confirmPassword = createUserRequest.get("confirmPassword");
        String userType = createUserRequest.get("role");
        String projectName = createUserRequest.get("project");

        User user = User.createUser(username, password, userType);

        if(password.equals(confirmPassword)) {
            user.setProject(projectService.findProjectsByTitle(projectName).get(0));

            Long tmpId = userService.saveUser(user);

            return true;
        }
        else
            return false;
    }

    /**
     * 유저 리스트
     * * @return
     */
    @GetMapping
    public List<User> listUsers() {
        return userService.findUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findOne(id);
    }

    /*
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    */

    /**
     * 유저 삭제 기능
     * @param deleteUserRequest
     */
    @DeleteMapping("/delete")
    public boolean deleteUser(@RequestBody Map<String, String> deleteUserRequest) {
        String username = deleteUserRequest.get("id");

        if(userService.findUserByName(username).isEmpty()){
            return false;
        }
        else{
            User user = userService.findUserByName(username).get(0);

            userService.deleteUser(user.getId());

            return true;
        }
    }


    //@CrossOrigin // 크롬과 같은 브라우져에서 보안 때문에 요청을 막는걸 허용해주는 어노테이션인 것 같습니다.

    /**
     * 로그인 기능
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public boolean login(@RequestBody Map<String, String> loginRequest) {
        String userName = loginRequest.get("id");
        String userPW = loginRequest.get("password");

        if(userService.authenticate(userName, userPW)) {
            projectService.updateCurrentUserName(userName);

            return true;
        }
        else if(userName.equals(Admin.id)){
            if(userPW.equals(Admin.pw))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    /**
     * 어드민인지 확인
     * @param usernameRequest
     * @return
     */
    @PostMapping("/isAdmin")
    public boolean isAdmin(@RequestBody Map<String, String> usernameRequest) {
        String userName = usernameRequest.get("id");

        return userName.equals(Admin.id);
    }
}
