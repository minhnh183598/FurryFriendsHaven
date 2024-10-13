package org.demo.huyminh.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.TagResponse;
import org.demo.huyminh.DTO.Request.TagRequest;
import org.demo.huyminh.Service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Minh
 * Date: 10/11/2024
 * Time: 3:42 PM
 */

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

    @GetMapping("/{tagName}")
    public ApiResponse<TagResponse> getTag(@PathVariable String tagName) {
        return ApiResponse.<TagResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get tag successfully")
                .result(tagService.getTag(tagName))
                .build();
    }

    @DeleteMapping("/{tagName}")
    public ApiResponse<String> deleteTag(@PathVariable String tagName) {
        tagService.deleteTag(tagName);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Delete tag successfully")
                .build();
    }

    @PutMapping
    public ApiResponse<TagResponse> updateTag(@RequestBody TagRequest request) {
        return ApiResponse.<TagResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update tag successfully")
                .result(tagService.updateTag(request))
                .build();
    }
}