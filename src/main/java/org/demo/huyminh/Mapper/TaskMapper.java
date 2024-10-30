package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.TaskResponse;
import org.demo.huyminh.DTO.Request.TaskCreationRequest;
import org.demo.huyminh.DTO.Request.TaskUpdateRequest;
import org.demo.huyminh.Entity.Tag;
import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 1:49 PM
 */

@Mapper(componentModel = "Spring")
public interface TaskMapper {

    @Mapping(target = "feedbacks", ignore = true)
    Task toTask(TaskCreationRequest request);

    @Mapping(target = "issues", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "adopter", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "petName", ignore = true)
    TaskResponse toTaskResponse(Task task);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "issues", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "tags", ignore = true)
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

    default List<String> mapTagsToString(Set<Tag> status) {
        if (status == null) {
            return Collections.emptyList();
        }
        return status.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }
}
