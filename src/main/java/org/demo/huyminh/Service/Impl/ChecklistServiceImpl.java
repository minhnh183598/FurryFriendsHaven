package org.demo.huyminh.Service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Checklist;
import org.demo.huyminh.Entity.ChecklistItem;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Enums.Status;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.ChecklistItemRepository;
import org.demo.huyminh.Repository.ChecklistRepository;
import org.demo.huyminh.Service.ChecklistService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * @author Minh
 * Date: 10/25/2024
 * Time: 1:51 AM
 */

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') || hasRole('VOLUNTEER')")
public class ChecklistServiceImpl implements ChecklistService {

    ChecklistItemRepository checklistItemRepository;
    ChecklistRepository checklistRepository;

    @Transactional
    @Override
    public void updateChecklistItem(int checklistItemId, int checklistId, boolean completed) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_NOT_FOUND));

        ChecklistItem entry = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_ITEM_NOT_FOUND));

        if(!checklist.getChecklistItems().contains(entry)) {
            throw new AppException(ErrorCode.ENTRY_NOT_IN_CHECKLIST);
        }

        if(checklist.getTask().getStatus().equals(Status.DONE)) {
            throw new AppException(ErrorCode.TASK_WAS_DONE);
        }

        ChecklistItem item = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_ITEM_NOT_FOUND));

        item.setCompleted(completed);
        checklistItemRepository.save(item);
    }

    @Transactional
    @Override
    public void deleteChecklistItem(int checklistItemId, int checklistId, User user) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_NOT_FOUND));

        ChecklistItem entry = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_ITEM_NOT_FOUND));

        if(!checklist.getChecklistItems().contains(entry)) {
            throw new AppException(ErrorCode.ENTRY_NOT_IN_CHECKLIST);
        }

        if(!checklist.getTask().getTeam().contains(user)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_DELETE_CHECKLIST_ITEM);
        }

        checklist.getChecklistItems().remove(entry);
        checklistRepository.save(checklist);
        checklistItemRepository.delete(entry);
    }
}
