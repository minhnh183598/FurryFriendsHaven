package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Service.ChecklistService;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Minh
 * Date: 10/25/2024
 * Time: 1:59 AM
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/checklists")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChecklistController {

    ChecklistService checklistService;
    UserService userService;

    @PutMapping("/{id}/entry")
    public ApiResponse<Void> updateChecklistItem(
            @PathVariable int id,
            @RequestParam(value = "entryId") int entryId,
            @RequestParam(value = "completed") boolean completed
    ) {

        checklistService.updateChecklistItem(entryId, id, completed);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Update checklist item successfully")
                .build();
    }

    @DeleteMapping("/{id}/entry")
    public ApiResponse<Void> deleteChecklistItem (
            @PathVariable int id,
            @RequestParam(value = "entryId") int entryId,
            @RequestHeader("Authorization") String token
    ) {

        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);
        checklistService.deleteChecklistItem(entryId, id, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete checklist item successfully")
                .build();
    }
}
