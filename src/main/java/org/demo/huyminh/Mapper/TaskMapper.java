package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.TaskResponse;
import org.demo.huyminh.DTO.Request.TaskCreationRequest;
import org.demo.huyminh.DTO.Request.TaskUpdateRequest;
import org.demo.huyminh.Entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 1:49 PM
 */

@Mapper(componentModel = "Spring")
public interface TaskMapper {

    Task toTask(TaskCreationRequest request);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "issues", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "team", ignore = true)
    TaskResponse toTaskResponse(Task task);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "issues", ignore = true)
    Task updateTask(TaskUpdateRequest request);
}
