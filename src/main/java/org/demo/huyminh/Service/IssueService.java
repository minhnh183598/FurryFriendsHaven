package org.demo.huyminh.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.IssueResponse;
import org.demo.huyminh.DTO.Request.IssueRequest;
import org.demo.huyminh.Entity.Issue;
import org.demo.huyminh.Entity.Tag;
import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Enums.Status;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.IssueMapper;
import org.demo.huyminh.Mapper.UserMapper;
import org.demo.huyminh.Repository.IssueRepository;
import org.demo.huyminh.Repository.TagRepository;
import org.demo.huyminh.Repository.TaskRepository;
import org.demo.huyminh.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 1:34 PM
 */

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class IssueService {

    IssueRepository issueRepository;
    TaskRepository taskRepository;
    UserRepository userRepository;
    IssueMapper issueMapper;
    TagRepository tagRepository;
    private final UserMapper userMapper;

    public Issue getIssueById(int issueId) {
        Optional<Issue> issue = issueRepository.findById(issueId);

        if(!issue.isPresent()) {
            throw new AppException(ErrorCode.ISSUE_NOT_FOUND);
        }

        return issue.get();
    }

    public List<Issue> getIssuesByTasId(int taskId) {
        List<Issue> issues = issueRepository.findByTaskId(taskId);
        if(taskRepository.findById(taskId).isEmpty()) {
            throw new AppException(ErrorCode.TASK_NOT_EXISTS);
        }

        if(issues.isEmpty()) {
            throw new AppException(ErrorCode.TASK_HAS_NO_ISSUES);
        }

        return issues;
    }

    public IssueResponse createIssue(IssueRequest request, User user, int taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if(!task.getTeam().contains(user)) {
            throw new AppException(ErrorCode.USER_NOT_IN_TEAM);
        }

        if(request.getDueDate().isAfter(task.getDueDate().toLocalDate())) {
            throw new AppException(ErrorCode.DUE_DATE_IS_BEFORE_TASK_DUE_DATE);
        }

        Issue issue = Issue.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(Status.fromString(request.getStatus()))
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .taskID(taskId)
                .task(task)
                .reporter(user)
                .tags(request.getTags())
                .build();
        saveIssueWithTags(issue);

        issueRepository.save(issue);
        task.getIssues().add(issue);
        taskRepository.save(task);

        IssueResponse result = issueMapper.toIssueResponse(issue);
        result.setTaskId(taskId);
        result.setTags(issue.getTags().stream().map(Tag::getName).toList());
        result.setReporter(userMapper.toUserResponse(user));

        return result;
    }

    public void updateIssue(IssueRequest request, int issueId, int taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new AppException(ErrorCode.ISSUE_NOT_FOUND));

        if(!task.getTeam().contains(user)) {
            throw new AppException(ErrorCode.USER_NOT_IN_TEAM);
        }

        if(!task.getIssues().contains(issue)) {
            throw new AppException(ErrorCode.ISSUE_NOT_IN_TASK);
        }

        if(!issue.getReporter().equals(user)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_UPDATE_ISSUE);
        }

        if(request.getDueDate().isAfter(task.getDueDate().toLocalDate())) {
            throw new AppException(ErrorCode.DUE_DATE_IS_BEFORE_TASK_DUE_DATE);
        }

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setStatus(Status.fromString(request.getStatus()));
        issue.setPriority(request.getPriority());
        issue.setDueDate(request.getDueDate());
        issue.setTask(task);

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            Set<Tag> existingTags = new HashSet<>(issue.getTags());

            for (Tag updatedTag : request.getTags()) {
                Optional<Tag> existingTag = tagRepository.findById(updatedTag.getName());

                if(!existingTag.isEmpty() && existingTag.get().getType().equals(Tag.TagType.ISSUE_LABEL)) {
                    existingTags.add(existingTag.get());
                }
            }
            issue.setTags(existingTags);
        }

        issueRepository.save(issue);
    }

    public void saveIssueWithTags(Issue issue) {
        if (issue.getTags() == null || issue.getTags().isEmpty()) {
            throw new AppException(ErrorCode.ISSUE_HAS_NO_TAGS);
        }

        Set<Tag> existingTags = new HashSet<>();

        for (Tag tag : issue.getTags()) {
            Optional<Tag> existingTag = tagRepository.findById(tag.getName());

            if(existingTag.isPresent() && existingTag.get().getType().equals(Tag.TagType.ISSUE_LABEL)) {
                existingTags.add(existingTag.get());
            }
        }

        if(existingTags.isEmpty()) {
            throw new AppException(ErrorCode.TAGS_NOT_EXISTS);
        }

        issue.setTags(existingTags);
    }

    public void deleteIssue(int issuedId, int taskId, User user) throws AppException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        Issue issue = issueRepository.findById(issuedId)
                .orElseThrow(() -> new AppException(ErrorCode.ISSUE_NOT_FOUND));

        if(!task.getIssues().contains(issue)) {
            throw new AppException(ErrorCode.ISSUE_NOT_IN_TASK);
        }

        if(!(issue.getReporter().equals(user))) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_DELETE_ISSUE);
        }

        issueRepository.delete(issue);
    }

    public void addUserToIssue(int issueId, int taskId, User user, String userId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new AppException(ErrorCode.ISSUE_NOT_FOUND));

        User newAssignee;

        if (userId != null) {
            newAssignee = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        } else if (username != null) {
            newAssignee = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        } else {
            throw new AppException(ErrorCode.PARAMETER_INVALID);
        }

        if(!task.getIssues().contains(issue)) {
            throw new AppException(ErrorCode.ISSUE_NOT_IN_TASK);
        }

        if (!task.getTeam().contains(user)) {
            throw new AppException(ErrorCode.USER_NOT_IN_TEAM);
        }

        if(!(issue.getReporter().equals(user) || issue.getAssignees().contains(user))) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_ADD_USER_TO_ISSUE);
        }

        issue.getAssignees().add(newAssignee);
        issueRepository.save(issue);
    }

    public Issue updateStatus(int issueId, int taskId, String status, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new AppException(ErrorCode.ISSUE_NOT_FOUND));

        if(!task.getIssues().contains(issue)) {
            throw new AppException(ErrorCode.ISSUE_NOT_IN_TASK);
        }

        if(!(issue.getReporter().equals(user) || issue.getAssignees().contains(user))) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_UPDATE_STATUS);
        }

        issue.setStatus(Status.fromString(status));
        return issueRepository.save(issue);
    }
}