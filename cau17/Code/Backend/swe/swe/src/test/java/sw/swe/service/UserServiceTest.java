package sw.swe.service;

import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sw.swe.domain.User;
import sw.swe.repository.UserRepository;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    // Junit 실핼할 때 스프링과 엮어서 실행
@SpringBootTest                 // 스프링부트를 띄운 상태로 테스트를 진행
@Transactional                  // 실제로 DB에 insert 문을 삽입하지 않고 Rollback함. 테스트를 용이하게 해준다.
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    //@Rollback(false)
    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUserName("testUser");
        user.setUserPW("password");
        user.setUserType("regular");

        Long userId = userService.saveUser(user);
        assertNotNull(userId);
    }

    @Test
    public void testFindUserById() {
        Long userId = 1L; // 존재하는 유저 ID로 대체
        User user = userService.findOne(userId);
        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    public void testAuthenticate() {
        String username = "userA"; // 존재하는 유저명으로 대체
        String password = "userA"; // 해당 유저의 비밀번호로 대체
        assertTrue(userService.authenticate(username, password));
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L; // 삭제할 유저 ID로 대체
        userService.deleteUser(userId);
        assertNull(userService.findOne(userId));
    }


}