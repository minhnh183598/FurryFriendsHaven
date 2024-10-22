package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.TagResponse;
import org.demo.huyminh.DTO.Request.TagRequest;
import org.demo.huyminh.Entity.Tag;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.TagMapper;
import org.demo.huyminh.Repository.TagRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 10:40 PM
 */

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TagService {

    TagRepository tagRepository;
    TagMapper tagMapper;

    public TagResponse createTag(TagRequest request) {
        if (tagRepository.existsById(request.getName())) {
            throw new AppException(ErrorCode.TAG_ALREADY_EXISTS);
        }
        Tag tag = tagMapper.toTag(request);
        if (!(tag.getType() == Tag.TagType.TASK_LABEL || tag.getType() == Tag.TagType.ISSUE_LABEL)) {
            throw new AppException(ErrorCode.INVALID_TAG_TYPE);
        }

        tagRepository.save(tag);
        return tagMapper.toTagResponse(tag);
    }

    public List<TagResponse> getAll() {
        List<TagResponse> tags = tagRepository.findAll()
                .stream().map(tagMapper::toTagResponse).toList();
        if (tags.isEmpty()) {
            throw new AppException(ErrorCode.LIST_TAG_IS_EMPTY);
        }
        return tags;
    }

    public void deleteTag(String tagName, String type) {
        Tag tag = tagRepository.findByIdAndType(tagName, type.toUpperCase())
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTS));
        try {
            tagRepository.deleteById(tag.getName());
        } catch (Exception e) {
            throw new AppException(ErrorCode.TAG_WAS_ON_USE);
        }
    }

    public TagResponse getTag(String tagName) {
        Tag tag = tagRepository.findById(tagName)
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTS));
        return tagMapper.toTagResponse(tag);
    }

    public List<TagResponse> getIssueTags(String type) {
        List<Tag> tags = tagRepository.findTagByType(type);
        if (tags.isEmpty()) {
            throw new AppException(ErrorCode.LIST_TAG_IS_EMPTY);
        }
        return tags.stream().map(tagMapper::toTagResponse).collect(Collectors.toList());
    }

    public List<TagResponse> getTaskTags(String type) {
        List<Tag> tags = tagRepository.findTagByType(type);
        if (tags.isEmpty()) {
            throw new AppException(ErrorCode.LIST_TAG_IS_EMPTY);
        }
        return tags.stream().map(tagMapper::toTagResponse).collect(Collectors.toList());
    }

    @Transactional
    public TagResponse updateTag(TagRequest request, String tagName, String type) {
        tagRepository.findByIdAndType(tagName, type.toUpperCase())
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTS));

        Tag.TagType tagType;
        try {
            tagType = Tag.TagType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_TAG_TYPE);
        }

        if (!(tagType == Tag.TagType.TASK_LABEL || tagType == Tag.TagType.ISSUE_LABEL)) {
            throw new AppException(ErrorCode.INVALID_TAG_TYPE);
        }


        if (tagRepository.existsTagByDescription(request.getDescription())) {
            tagRepository.findByIdAndType(request.getName(), request.getType().toUpperCase())
                    .orElseThrow(() -> new AppException(ErrorCode.TAG_ALREADY_EXISTS));
        }

        Tag tag = tagMapper.toTag(request);
        try {
            tagRepository.update(tag.getName(), tag.getDescription(), tag.getType().toString(), tagName);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TAG_WAS_ON_USE);
        }
        return tagMapper.toTagResponse(tag);
    }
}
