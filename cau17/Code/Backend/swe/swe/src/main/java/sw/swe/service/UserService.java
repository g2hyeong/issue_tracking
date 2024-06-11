package sw.swe.service;

import lombok.RequiredArgsConstructor;
import sw.swe.domain.*;
import sw.swe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    /**
     * 유저를 DB에 저장(중복 확인)
     */
    @Transactional
    public Long saveUser(User user) {
        validateDuplicateUser(user); // 중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        List<User> findUsers = userRepository.findByName(user.getUserName());
        System.out.println(findUsers.isEmpty());
        if (!findUsers.isEmpty() || user.getUserName().equals(Admin.id) ) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    /**
     * 유저 전체 조회
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    /**
     * 유저 고유 Id를 통한 조회
     */
    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }

    /**
     * 유저 이름(userName, 로그인 할떄 그 id)을 통한 조회
     */
    public List<User> findUserByName(String userName) {return userRepository.findByName(userName);}

    /**
     * 유저의 로그인한 id와 pw가 유효한지 확인
     */
    public boolean authenticate(String username, String password) {
        List<User> userlist = userRepository.findByName(username);
        if(!userlist.isEmpty()) {
            User user = userlist.get(0);
            if(user.getUserPW().equals(password))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    /**
     * 해당 유저가 Admin인지 확인
     */
    public boolean isAdmin(Long userId) {
        if(Objects.equals(userRepository.findOne(userId).getUserType(), "Admin")){
            return true;
        }
        else
            return false;
    }

    /**
     * 해당 유저의 유저타입(role) 반환
     */
    public String getUsertype(Long userId) {
        User user = userRepository.findOne(userId);

        return user.getUserType();
    }

    /**
     * 유저 삭제
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findOne(userId);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }
}
