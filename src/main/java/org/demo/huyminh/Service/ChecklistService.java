package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Checklist;
import org.demo.huyminh.Entity.ChecklistItem;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.ChecklistItemRepository;
import org.demo.huyminh.Repository.ChecklistRepository;
import org.hibernate.annotations.DialectOverride;
import org.springframework.stereotype.Service;

/**
 * @author Minh
 * Date: 10/25/2024
 * Time: 1:51 AM
 */

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChecklistService {

    ChecklistItemRepository checklistItemRepository;
    ChecklistRepository checklistRepository;

    @Transactional
    public void updateChecklistItem(int checklistItemId, int checklistId, boolean completed) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_NOT_FOUND));

        ChecklistItem entry = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_ITEM_NOT_FOUND));

        if(!checklist.getChecklistItems().contains(entry)) {
            throw new AppException(ErrorCode.ENTRY_NOT_IN_CHECKLIST);
        }

        ChecklistItem item = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_ITEM_NOT_FOUND));

        item.setCompleted(completed);
        checklistItemRepository.save(item);
    }
}
