package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.TaskResponse;
import org.demo.huyminh.DTO.Request.TaskCreationRequest;
import org.demo.huyminh.DTO.Request.TaskUpdateRequest;
import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Enums.Status;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 1:49 PM
 */

@Mapper(componentModel = "Spring")
public interface TaskMapper {

    @Mapping(target = "feedbacks", ignore = true)
    Task toTask(TaskCreationRequest request);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "issues", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    TaskResponse toTaskResponse(Task task);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "issues", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    Task updateTask(TaskUpdateRequest request);

    default LocalDateTime mapStringToLocalDateTime(String dueDate) {
        if (dueDate == null) {
            throw new AppException(ErrorCode.DUE_DATE_IS_REQUIRED);
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(dueDate, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format: " + dueDate);
        }
    }

    default String mapTaskStatusToString(Status status) {
        return status != null ? status.name() : null;
    }

    default Status mapStringToTaskStatus(String status) {
        if (status == null) {
            throw new AppException(ErrorCode.STATUS_IS_REQUIRED);
        }

        try {
            return Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
    }
}
