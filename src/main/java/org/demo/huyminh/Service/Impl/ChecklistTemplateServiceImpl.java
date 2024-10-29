package org.demo.huyminh.Service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Request.ItemTemplateRequest;
import org.demo.huyminh.DTO.Request.TemplateCreationRequest;
import org.demo.huyminh.DTO.Request.TemplateUpdateRequest;
import org.demo.huyminh.Entity.ChecklistItemTemplate;
import org.demo.huyminh.Entity.ChecklistTemplate;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.ChecklistTemplateRepository;
import org.demo.huyminh.Service.ChecklistTemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Minh
 * Date: 10/24/2024
 * Time: 6:21 PM
 */

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class ChecklistTemplateServiceImpl implements ChecklistTemplateService {

    ChecklistTemplateRepository checklistTemplateRepository;

    @Override
    public List<ChecklistTemplate> getChecklistTemplates() {
        return checklistTemplateRepository.findAll();
    }

    @Override
    public ChecklistTemplate createChecklistTemplate(TemplateCreationRequest templateCreationRequest) {
        ChecklistTemplate template = ChecklistTemplate.builder()
                .name(templateCreationRequest.getName())
                .items(new ArrayList<>())
                .build();

        for (ItemTemplateRequest itemRequest : templateCreationRequest.getItems()) {
            ChecklistItemTemplate item = ChecklistItemTemplate.builder()
                    .entry(itemRequest.getEntry())
                    .template(template)
                    .build();

            template.getItems().add(item);
        }

        try {
            return checklistTemplateRepository.save(template);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CHECKLIST_TEMPLATE_EXISTS);
        }
    }

    @Transactional
    @Override
    public void updateChecklistTemplate(TemplateUpdateRequest templateUpdateRequest) {
        ChecklistTemplate template = checklistTemplateRepository.findById(templateUpdateRequest.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_TEMPLATE_NOT_EXISTS));

        template.setName(templateUpdateRequest.getName());
        template.setItems(templateUpdateRequest.getItems());
        checklistTemplateRepository.save(template);
    }

    @Transactional
    @Override
    public void deleteCheckListTemplate(String name, int id) {
        if(name == null && id == 0) {
            throw new AppException(ErrorCode.CHECKLIST_TEMPLATE_NOT_EXISTS);
        }

        ChecklistTemplate template;
        if(name != null) {
            template = checklistTemplateRepository.findByName(name)
                    .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_TEMPLATE_NOT_EXISTS));
            checklistTemplateRepository.deleteById(template.getId());
        } else {
            template = checklistTemplateRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_TEMPLATE_NOT_EXISTS));
            checklistTemplateRepository.deleteById(id);
        }
    }

    @Override
    public ChecklistTemplate getChecklistSpecificTemplate(String name, int id) {
        if(name == null && id == 0) {
            throw new AppException(ErrorCode.CHECKLIST_TEMPLATE_NOT_EXISTS);
        }

        if(name != null) {
            return checklistTemplateRepository.findByName(name)
                    .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_TEMPLATE_NOT_EXISTS));
        }
        return checklistTemplateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_TEMPLATE_NOT_EXISTS));
    }
}
