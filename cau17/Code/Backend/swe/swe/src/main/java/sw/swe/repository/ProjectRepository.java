package sw.swe.repository;

import org.springframework.stereotype.Repository;
import sw.swe.domain.Project;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProjectRepository {

    @PersistenceContext
    private EntityManager em;

    // Project 객체를 받아 DB에 저장.
    public void save(Project project) {
        em.persist(project);
    }

    // Project 객체의 고유 id값을 통해 해당하는 프로젝트를 반환함
    public Project findOne(Long id) {
        return em.find(Project.class, id);
    }

    // DB에 저장된 모든 Project 객체 반환
    public List<Project> findAll() {
        return em.createQuery("select p from Project p", Project.class)
                .getResultList();
    }

    // Project의 title값을 통해 해당하는 Project 객체(들)을 반환함.
    public List<Project> findByTitle(String title) {
        return em.createQuery("select p from Project p where p.title = :title", Project.class)
                .setParameter("title", title)
                .getResultList();
    }

    public void delete(Project project) {
        em.remove(project);
    }

    public void updateCurrentUserName(String username) {
        em.createQuery("UPDATE Project p SET p.currentUserName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }
}
