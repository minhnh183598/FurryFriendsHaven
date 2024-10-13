package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.IssueResponse;
import org.demo.huyminh.DTO.Request.IssueRequest;
import org.demo.huyminh.Entity.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:11 PM
 */

@Mapper(componentModel = "Spring")
public interface IssueMapper {

    @Mapping(target = "reporter", ignore = true)
    Issue toIssue(IssueRequest request);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "reporter", ignore = true)
    IssueResponse toIssueResponse(Issue issue);
}