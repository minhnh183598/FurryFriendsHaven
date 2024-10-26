package org.demo.huyminh.Service;

import org.demo.huyminh.DTO.Reponse.BriefTaskResponse;
import org.demo.huyminh.DTO.Reponse.TaskResponse;
import org.demo.huyminh.DTO.Request.FeedbackCreationRequest;
import org.demo.huyminh.DTO.Request.InvitationEventData;
import org.demo.huyminh.DTO.Request.TaskCreationRequest;
import org.demo.huyminh.DTO.Request.TaskUpdateRequest;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Enums.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 10:00 PM
 */

public interface TaskService {
    TaskResponse createTask(TaskCreationRequest task, User user);

    List<BriefTaskResponse> getTaskByTeam(User user);

    List<BriefTaskResponse> searchAndSortTasks(String category, Status status, LocalDateTime dueDate, String order, String keyword);

    TaskResponse getTaskByApplicationId(String applicationId, User user);

    TaskResponse getTaskById(int taskId);

    void deleteTask(int taskId, User user);

    TaskResponse updateTask(TaskUpdateRequest updatedTask, User user);

    void changeStatus(int taskId, String status, User user);

    void attendToTask(int taskId, User user);

    void addUserToTask(int taskId, String userId, User existingUser);

    void removeUserFromTask(int taskId, String userId, User user);

    List<BriefTaskResponse> searchTask(String keyword);

    List<BriefTaskResponse> getAllTasks();

    InvitationEventData inviteUserToTask(int taskId, String username, User user);

    String acceptInvitation(int taskId, String username, User user, String choice);
}
