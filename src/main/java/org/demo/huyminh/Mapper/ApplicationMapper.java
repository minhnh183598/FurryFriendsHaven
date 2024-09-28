package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.ApplicationResponse;
import org.demo.huyminh.DTO.Request.ApplicationCreationRequest;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface ApplicationMapper {
    Application toApplication(ApplicationCreationRequest request);
    ApplicationResponse toApplicationResponse(Application application);
    void updateApplication(@MappingTarget Application application, ApplicationUpdateRequest request);
}
