package sw.swe.repository;

import org.springframework.stereotype.Repository;
import sw.swe.domain.Issue;
import sw.swe.domain.IssueStatus;

import jakarta.persistence.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@Repository
public class IssueStatusRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(IssueStatus status) {
        em.persist(status);
    }

    public IssueStatus findOne(Long id) {
        return em.find(IssueStatus.class, id);
    }

    public List<IssueStatus> findByAssignee(String username) {
        return em.createQuery("select s from IssueStatus s where s.assignee = :username", IssueStatus.class)
                .setParameter("username", username)
                .getResultList(); }

    public List<IssueStatus> findByStatus(String status) {
        return em.createQuery("select s from IssueStatus s where s.status = :status", IssueStatus.class)
                .setParameter("status", status)
                .getResultList(); }

    public List<IssueStatus> findAll() {
        return em.createQuery("select s from IssueStatus s", IssueStatus.class)
                .getResultList();
    }

    public void updateAssignee(String username, Long issue_id) {
        em.createQuery("UPDATE IssueStatus IS SET IS.assignee = :username WHERE IS.issue.id = :issue_id")
                .setParameter("username", username)
                .setParameter("issue_id", issue_id)
                .executeUpdate();
    }

    public void updateStatus(Long issue_id, String status) {
        em.createQuery("UPDATE IssueStatus IS SET IS.status = :status WHERE IS.issue.id = :issue_id")
                .setParameter("status", status)
                .setParameter("issue_id", issue_id)
                .executeUpdate();
    }

    public void updateFixed(Long issue_id, String username) {
        em.createQuery("UPDATE IssueStatus IS SET IS.isFixed = true, IS.fixer = :fixer WHERE IS.issue.id = :issue_id")
                .setParameter("fixer", username)
                .setParameter("issue_id", issue_id)
                .executeUpdate();
    }

    public List<String> findTopFixers(Long project_id) {
        // 1. Find top fixers by count
        TypedQuery<Object[]> query = em.createQuery(
                "SELECT is.fixer, COUNT(is.fixer) as fixCount " +
                        "FROM IssueStatus is " +
                        "WHERE is.issue.project.id = :project_id " +
                        "GROUP BY is.fixer " +
                        "ORDER BY fixCount DESC", Object[].class)
                .setParameter("project_id", project_id);
        query.setMaxResults(10); // 일단 상위 10명까지 가져오기

        List<Object[]> results = query.getResultList();

        // 2. Extract fixer names from the query results
        List<String> topFixers = new ArrayList<>();
        for (Object[] result : results) {
            topFixers.add((String) result[0]);
        }

        return topFixers;
    }

    public List<String> findUnassignedUsers(List<String> topFixers) {
        // 3. Find currently unassigned fixers
        TypedQuery<String> query = em.createQuery(
                    "SELECT u.userName " +
                        "FROM User u " +
                        "WHERE u.userName IN :topFixers AND u.userName NOT IN " +
                        "(SELECT is.assignee FROM IssueStatus is WHERE is.assignee IS NOT NULL " +
                        "AND (is.status != 'fixed' AND is.status != 'resolved' AND is.status != 'closed'))", String.class);
        query.setParameter("topFixers", topFixers);

        return query.getResultList();
    }

    public List<String> recommendFixers(Long project_id) {
        List<String> topFixers = findTopFixers(project_id);
        List<String> unassignedFixers = findUnassignedUsers(topFixers);
        System.out.println(unassignedFixers);

        // 4. Return the top 3 unassigned fixers
        if (unassignedFixers.size() > 3) {
            return unassignedFixers.subList(0, 3);
        } else {
            return unassignedFixers;
        }
    }

    public void delete(IssueStatus status) {
        em.remove(status);
    }

    public void saveIssue(Issue issue) {
        em.persist(issue);
    }
}
