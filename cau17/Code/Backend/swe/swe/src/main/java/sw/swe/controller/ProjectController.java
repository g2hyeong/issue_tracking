package sw.swe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sw.swe.domain.Project;
import sw.swe.domain.User;
import sw.swe.service.ProjectService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 프로젝트 생성
     * @param createProjectRequest
     * @return
     */
    @PostMapping("/create")
    public boolean createUser(@RequestBody Map<String, String> createProjectRequest) {
        String title = createProjectRequest.get("title");
        String description = createProjectRequest.get("description");

        Project project = Project.createProject(title, description, null);

        Long tmpId = projectService.saveProject(project);

        return true;
    }

    @GetMapping
    public List<Project> listProjects() {
        return projectService.findAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable Long id) {
        return projectService.findOne(id);
    }

    /**
     * 프로젝트 삭제 기능
     * @param deleteProjectRequest
     */
    @DeleteMapping("/delete")
    public boolean deleteProject(@RequestBody Map<String, String> deleteProjectRequest) {
        Long project_id = Long.parseLong(deleteProjectRequest.get("project_id"));

        if(projectService.findOne(project_id) != null){
            projectService.deleteProject(project_id);

            return true;
        }
        else{
            return false;
        }
    }
}
