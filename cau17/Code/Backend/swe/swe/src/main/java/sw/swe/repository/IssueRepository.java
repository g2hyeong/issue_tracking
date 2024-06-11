package sw.swe.repository;

import org.springframework.stereotype.Repository;
import sw.swe.domain.Issue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sw.swe.domain.Project;
import sw.swe.domain.User;

import java.util.List;

@Repository
public class IssueRepository {

    @PersistenceContext
    private EntityManager em;

    // Issue 객체를 받아 DB에 저장. (persist는 영속하여 저장함을 의미함)
    public void save(Issue issue) {
        em.persist(issue);
    }

    // Issue 객체의 고유 id값을 통해 해당하는 이슈를 반환함
    public Issue findOne(Long id) {
        return em.find(Issue.class, id);
    }

    // DB에 저장된 모든 Issue 객체 반환
    public List<Issue> findAll() {
        return em.createQuery("select i from Issue i", Issue.class)
                .getResultList();
    }

    // Project의 ID를 통해 해당 프로젝트의 Issue 객체(들)을 반환함.
    public List<Issue> findByProjectId(Long projectId) {
        return em.createQuery("select i from Issue i where i.project.id = :projectId", Issue.class)
                .setParameter("projectId", projectId)
                .getResultList();
    }

    public List<Issue> findByAssignee(String assignee) {
        return em.createQuery("SELECT i FROM Issue i JOIN i.status s WHERE LOWER(s.assignee) LIKE LOWER(:assignee)", Issue.class)
                .setParameter("assignee", "%" + assignee + "%")
                .getResultList();
    }

    public List<Issue> findByStatus(String status) {
        return em.createQuery("SELECT i FROM Issue i JOIN i.status s WHERE LOWER(s.status) LIKE LOWER(:status)", Issue.class)
                .setParameter("status", "%" + status + "%")
                .getResultList();
    }

    public List<Issue> findByReporter(String reporter) {
        return em.createQuery("SELECT i FROM Issue i WHERE LOWER(i.reporter) LIKE LOWER(:reporter)", Issue.class)
                .setParameter("reporter", "%" + reporter + "%")
                .getResultList();
    }

    public List<Issue> findByTitle(String title) {
        return em.createQuery("SELECT i FROM Issue i WHERE LOWER(i.title) LIKE LOWER(:title)", Issue.class)
                .setParameter("title", "%" + title + "%")
                .getResultList();
    }

    public List<Issue> findByID(String title) {
        return em.createQuery("select i from Issue i where i.title = :title", Issue.class)
                .setParameter("title", title)
                .getResultList();
    }

    public void delete(Issue issue) {
        em.remove(issue);
    }

    public void saveProject(Project project) {
        em.persist(project);
    }

}
