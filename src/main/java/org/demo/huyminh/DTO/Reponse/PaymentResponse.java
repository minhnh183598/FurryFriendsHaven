package org.demo.huyminh.DTO.Reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    String amount;
    String bankTranId;
    String orderInfo;
    String payDate;
    String status;
    Pet pet;
    UserResponse user;
}
