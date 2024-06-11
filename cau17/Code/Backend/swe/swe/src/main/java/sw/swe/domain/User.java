package sw.swe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sw.swe.service.UserService;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String userName;
    private String userPW;
    private String userType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore // Json 출력시에 이 필드는 출력되지 않음
    private Project project;

    @JsonProperty("project_id")
    public Long getProjectID() {
        return project != null ? project.getId() : null;
    }

    public static User createUser(String userName, String userPW, String userType){
        User user = new User();
        user.setUserName(userName);
        user.setUserPW(userPW);
        user.setUserType(userType);

        return user;
    }

    public void setProject(Project project){
        this.project = project;
        project.getUserList().add(this);
    }
}
