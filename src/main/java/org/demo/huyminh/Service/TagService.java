package org.demo.huyminh.Service;

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
        tagRepository.save(tag);
        return tagMapper.toTagResponse(tag);
    }

    public List<TagResponse> getAll() {
        return tagRepository.findAll()
                .stream().map(tagMapper::toTagResponse).toList();
    }

    public void deleteTag(String tagName) {
        Tag tag = tagRepository.findById(tagName)
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTS));
        tagRepository.deleteById(tag.getName());
    }

    public TagResponse getTag(String tagName) {
        Tag tag = tagRepository.findById(tagName)
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTS));
        return tagMapper.toTagResponse(tag);
    }

    public TagResponse updateTag(TagRequest request) {
        tagRepository.findById(request.getName())
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTS));
        Tag tag = tagMapper.toTag(request);
        tagRepository.save(tag);
        return tagMapper.toTagResponse(tag);
    }
}
