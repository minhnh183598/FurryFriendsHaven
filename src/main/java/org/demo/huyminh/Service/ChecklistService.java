package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import org.demo.huyminh.Entity.User;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:40 PM
 */

public interface ChecklistService {
    void updateChecklistItem(int checklistItemId, int checklistId, boolean completed);

    void deleteChecklistItem(int checklistItemId, int checklistId, User user);
}
