package org.demo.huyminh.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Request.TemplateCreationRequest;
import org.demo.huyminh.DTO.Request.TemplateUpdateRequest;
import org.demo.huyminh.Entity.ChecklistTemplate;
import org.demo.huyminh.Service.ChecklistTemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Minh
 * Date: 10/24/2024
 * Time: 7:48 PM
 */


@Slf4j
@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChecklistTemplateController {

    ChecklistTemplateService checklistTemplateService;

    @GetMapping
    public ApiResponse<List<ChecklistTemplate>> getChecklistTemplates() {

        List<ChecklistTemplate> checklistTemplates = checklistTemplateService.getChecklistTemplates();
        return ApiResponse.<List<ChecklistTemplate>>builder()
                .code(HttpStatus.OK.value())
                .message("Get checklist templates successfully")
                .result(checklistTemplates)
                .build();
    }

    @GetMapping("/checklist")
    public ApiResponse<ChecklistTemplate> getChecklistSpecificTemplate(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "id", required = false, defaultValue = "0") int id
    ) {

        if (name == null && id == 0) {
            return ApiResponse.<ChecklistTemplate>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Name or id is required")
                    .build();
        }

        return ApiResponse.<ChecklistTemplate>builder()
                .code(HttpStatus.OK.value())
                .message("Get checklist template successfully")
                .result(checklistTemplateService.getChecklistSpecificTemplate(name, id))
                .build();
    }

    @PostMapping
    public ApiResponse<ChecklistTemplate> createChecklistTemplate(@RequestBody TemplateCreationRequest request) {
        checklistTemplateService.createChecklistTemplate(request);
        return ApiResponse.<ChecklistTemplate>builder()
                .code(HttpStatus.OK.value())
                .message("Create checklist template successfully")
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteChecklistTemplate(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "id", required = false, defaultValue = "0") int id
    ) {

        if (name == null && id == 0) {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Name or id is required")
                    .build();
        }

        checklistTemplateService.deleteCheckListTemplate(name, id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete checklist template successfully")
                .build();
    }

    @PutMapping
    public ApiResponse<ChecklistTemplate> updateChecklistTemplate(@RequestBody TemplateUpdateRequest request) {
        checklistTemplateService.updateChecklistTemplate(request);
        return ApiResponse.<ChecklistTemplate>builder()
                .code(HttpStatus.OK.value())
                .message("Update checklist template successfully")
                .build();
    }
}
