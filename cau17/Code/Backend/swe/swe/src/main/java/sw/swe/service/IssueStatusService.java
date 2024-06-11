package sw.swe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.swe.domain.Issue;
import sw.swe.domain.IssueStatus;
import sw.swe.repository.IssueStatusRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class IssueStatusService {

    @Autowired
    private IssueStatusRepository statusRepository;

    /**
     * 상태를 DB에 저장
     */
    @Transactional
    public Long saveStatus(IssueStatus status) {
        statusRepository.save(status);
        return status.getId();
    }

    /**
     * 상태를 이슈에 연결
     */
    @Transactional
    public void setIssueForIssueStatus(Long issueStatusId, Issue issue) {
        IssueStatus issueStatus = statusRepository.findOne(issueStatusId);
        if (issueStatus != null) {
            issueStatus.setIssue(issue);
        } else {
            throw new IllegalArgumentException("존재하지 않는 커멘트입니다.");
        }
    }


    /**
     * 상태 조회
     */

    public IssueStatus findOne(Long statusId) {
        return statusRepository.findOne(statusId);
    }

    /**
     * 전체 상태 조회
     */
    public List<IssueStatus> findAllStatuses() { return statusRepository.findAll(); }

    /**
     * Assignee 조회
     * @param assignee
     * @return
     */
    public List<IssueStatus> findByAssignee(String assignee) { return statusRepository.findByAssignee(assignee); }

    /**
     * Status 조회
     * @param status
     * @return
     */
    public List<IssueStatus> findByStatus(String status) { return statusRepository.findByStatus(status); }

    /**
     * 상태 삭제
     */
    @Transactional
    public void deleteStatus(Long statusId) {
        IssueStatus status = statusRepository.findOne(statusId);
        if (status != null) {
            statusRepository.delete(status);
        } else {
            throw new IllegalArgumentException("존재하지 않는 상태입니다.");
        }
    }

    public List<String> recommendAssignee(Long project_id){
        return statusRepository.recommendFixers(project_id);
    }

    @Transactional
    public void updateAssignee(String assignee, Long issueId) {
        statusRepository.updateAssignee(assignee, issueId);
    }

    @Transactional
    public void updateStatus(Long issueId, String status) {
        statusRepository.updateStatus(issueId, status);
    }

    @Transactional
    public void updateFixed(Long issueId, String username) {
        statusRepository.updateFixed(issueId, username);
    }
}
