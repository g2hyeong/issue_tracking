package sw.swe.service;

import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sw.swe.domain.Project;
import sw.swe.repository.ProjectRepository;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    // Junit 실핼할 때 스프링과 엮어서 실행
@SpringBootTest                 // 스프링부트를 띄운 상태로 테스트를 진행
@Transactional
public class ProjectServiceTest {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectService projectService;

    //@Rollback(false)
    @Test
    public void testSaveProject() {
        Project project = new Project();
        project.setTitle("Test Project");
        project.setDescription("This is a test project.");

        Long projectId = projectService.saveProject(project);
        assertNotNull(projectId);
    }

    @Test
    public void testFindProjectById() {
        Long projectId = 1L; // 존재하는 프로젝트 ID로 대체
        Project project = projectService.findOne(projectId);
        assertNotNull(project);
        assertEquals(projectId, project.getId());
    }

    @Test
    public void testDeleteProject() {
        Long projectId = 1L; // 삭제할 프로젝트 ID로 대체
        projectService.deleteProject(projectId);
        assertNull(projectService.findOne(projectId));
    }
}