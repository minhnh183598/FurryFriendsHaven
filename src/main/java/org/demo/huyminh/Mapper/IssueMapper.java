package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.IssueResponse;
import org.demo.huyminh.DTO.Reponse.UserResponse;
import org.demo.huyminh.DTO.Request.IssueRequest;
import org.demo.huyminh.Entity.Issue;
import org.demo.huyminh.Entity.Tag;
import org.demo.huyminh.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:11 PM
 */

@Mapper(componentModel = "Spring")
public interface IssueMapper {

    @Mapping(target = "comments", ignore = true)
    Issue toIssue(IssueRequest request);

    @Mapping(target = "reporter", expression = "java(toUserResponse(issue.getReporter()))")
    @Mapping(target = "tags", expression = "java(mapTagsToStrings(issue.getTags()))")
    IssueResponse toIssueResponse(Issue issue);

    default List<String> mapTagsToStrings(Set<Tag> tags) {
        if (tags == null) {
            return Collections.emptyList();
        }
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }


    default UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .createdAt(user.getCreatedAt())
                .build();
    }
}