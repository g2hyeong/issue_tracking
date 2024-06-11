package sw.swe.repository;

import org.springframework.stereotype.Repository;
import sw.swe.domain.Issue;
import sw.swe.domain.IssueComment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sw.swe.domain.Project;

import java.util.List;

@Repository
public class IssueCommentRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(IssueComment comment) {
        em.persist(comment);
    }

    public IssueComment findOne(Long id) {
        return em.find(IssueComment.class, id);
    }

    public List<IssueComment> findAll() {
        return em.createQuery("select c from IssueComment c", IssueComment.class)
                .getResultList();
    }
    public List<IssueComment> findByIssueId(Long issueId) {
        return em.createQuery("select i from IssueComment i where i.issue.id = :issueId", IssueComment.class)
                .setParameter("issueId", issueId)
                .getResultList();
    }
    public void delete(IssueComment comment) {
        em.remove(comment);
    }

    public void saveIssue(Issue issue) {
        em.persist(issue);
    }
}
