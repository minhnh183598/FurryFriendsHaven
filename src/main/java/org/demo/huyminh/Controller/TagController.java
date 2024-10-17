package org.demo.huyminh.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.TagResponse;
import org.demo.huyminh.DTO.Request.TagRequest;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Minh
 * Date: 10/11/2024
 * Time: 3:42 PM
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagController {

    final TagService tagService;

    @PostMapping
    public ApiResponse<TagResponse> createTag(@RequestBody TagRequest request) {
        return ApiResponse.<TagResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Tag created successfully")
                .result(tagService.createTag(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<TagResponse>> getAll() {
        return ApiResponse.<List<TagResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get tags successfully")
                .result(tagService.getAll())
                .build();
    }

    @GetMapping("/name/{nameTag}")
    public ApiResponse<TagResponse> getTag(@PathVariable("nameTag") String tagName) {
        return ApiResponse.<TagResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get tag successfully")
                .result(tagService.getTag(tagName))
                .build();
    }

    @GetMapping("/type/{typeTag}")
    public ApiResponse<List<TagResponse>> getTagByType(@PathVariable("typeTag") String type) {
        List<TagResponse> tags;

        if(type.equals("ISSUE_LABEL")) {
            tags = tagService.getIssueTags(type.toUpperCase());
        } else if (type.equals("TASK_LABEL")) {
            tags = tagService.getTaskTags(type.toUpperCase());
        } else {
            throw new AppException(ErrorCode.INVALID_TAG_TYPE);
        }
        return ApiResponse.<List<TagResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get issue tags successfully")
                .result(tags)
                .build();
    }

    @DeleteMapping("/{tagName}/{type}")
    public ApiResponse<String> deleteTag(
            @PathVariable("tagName") String tagName,
            @PathVariable("type") String type
    ) {
        tagService.deleteTag(tagName, type);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Delete tag successfully")
                .build();
    }

    @PutMapping("/{tagName}/{type}")
    public ApiResponse<TagResponse> updateTag(
            @RequestBody TagRequest request,
            @PathVariable("tagName") String tagName,
            @PathVariable("type") String type
    ) {
        log.info("Update endpoint in TagController 🗿");
        return ApiResponse.<TagResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update tag successfully")
                .result(tagService.updateTag(request, tagName, type))
                .build();
    }
}