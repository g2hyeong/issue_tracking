package sw.swe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.swe.domain.Project;
import sw.swe.repository.ProjectRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 프로젝트를 DB에 저장
     */
    @Transactional
    public Long saveProject(Project project) {
        projectRepository.save(project);
        return project.getId();
    }

    /**
     * 프로젝트 조회
     */
    public Project findOne(Long projectId) {
        return projectRepository.findOne(projectId);
    }

    /**
     * 전체 프로젝트 조회
     */
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * 제목으로 프로젝트 조회
     */
    public List<Project> findProjectsByTitle(String title) {
        return projectRepository.findByTitle(title);
    }

    /**
     * 프로젝트 삭제
     */
    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findOne(projectId);
        if (project != null) {
            projectRepository.delete(project);
        } else {
            throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
        }
    }

    @Transactional
    public void updateCurrentUserName(String username){
        projectRepository.updateCurrentUserName(username);
    }

}
