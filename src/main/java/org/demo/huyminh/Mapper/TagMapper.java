package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.TagResponse;
import org.demo.huyminh.DTO.Request.TagRequest;
import org.demo.huyminh.Entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:11 PM
 */

@Mapper(componentModel = "Spring")
public interface TagMapper {

    @Mapping(target = "tasks", ignore = true)
    Tag toTag(TagRequest request);

    TagResponse toTagResponse(Tag tag);
}