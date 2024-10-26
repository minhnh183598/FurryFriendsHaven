package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import org.demo.huyminh.DTO.Reponse.TagResponse;
import org.demo.huyminh.DTO.Request.TagRequest;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:59 PM
 */

public interface TagService {
    TagResponse createTag(TagRequest request);

    List<TagResponse> getAll();

    void deleteTag(String tagName, String type);

    TagResponse getTag(String tagName);

    List<TagResponse> getIssueTags(String type);

    List<TagResponse> getTaskTags(String type);

    TagResponse updateTag(TagRequest request, String tagName, String type);
}
