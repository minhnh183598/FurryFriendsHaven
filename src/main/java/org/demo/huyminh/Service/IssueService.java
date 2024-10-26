package org.demo.huyminh.Service;

import org.demo.huyminh.DTO.Reponse.IssueResponse;
import org.demo.huyminh.DTO.Request.IssueRequest;
import org.demo.huyminh.Entity.Issue;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Exception.AppException;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:51 PM
 */

public interface IssueService {
    List<IssueResponse> getIssuesByTasId(int taskId, String status, String sort);

    IssueResponse createIssue(IssueRequest request, User user, int taskId);

    void updateIssue(IssueRequest request, int issueId, int taskId, User user);

    void deleteIssue(int issuedId, int taskId, User user) throws AppException;

    Issue getIssueById(int issueId, int taskId, User user);

    void addUserToIssue(int issueId, int taskId, User user, String userId, String username);

    Issue updateStatus(int issueId, int taskId, String status, User user);
}
