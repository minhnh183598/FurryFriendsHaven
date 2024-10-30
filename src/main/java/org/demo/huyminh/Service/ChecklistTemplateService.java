package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import org.demo.huyminh.DTO.Request.TemplateCreationRequest;
import org.demo.huyminh.DTO.Request.TemplateUpdateRequest;
import org.demo.huyminh.Entity.ChecklistTemplate;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:44 PM
 */

public interface ChecklistTemplateService {
    List<ChecklistTemplate> getChecklistTemplates();

    ChecklistTemplate createChecklistTemplate(TemplateCreationRequest templateCreationRequest);

    void updateChecklistTemplate(TemplateUpdateRequest templateUpdateRequest);

    void deleteCheckListTemplate(String name, int id);

    ChecklistTemplate getChecklistSpecificTemplate(String name, int id);
}
