package org.demo.huyminh.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Request.PetCreationRequest;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

@Slf4j

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    // input
    private PetCreationRequest request;
    // output
    private Pet petResponse;

    // Có thể nhiều Testcase sẽ sử dụng đến
    // Hàm khởi tạo chung sẽ chạy trước khi những hàm Test được chạy
    @BeforeEach
    void initData(){
        // Khi tạo
        request = PetCreationRequest.builder()
                .petName("Guhalu").petType("Cat").petAge("Young").petBreed("Beagle")
                .petColor("Lemon").petDescription("A curious cat who loves to sniff around.")
                .petSize("40 cm").petWeight(11).petGender("FeMale").petVaccin("Yes").petStatus("Available")
                .build();

        //Mong muốn khi nhận về
        petResponse = Pet.builder()
                .petId("b20c8a31409c")
                .petName("Guhalu").petType("Cat").petAge("Young").petBreed("Beagle")
                .petColor("Lemon").petDescription("A curious cat who loves to sniff around.")
                .petSize("40 cm").petWeight(11).petGender("FeMale").petVaccin("Yes").petStatus("Available")
                .build();
    }

    @Test
    void createPet_validRequest_success() throws Exception {
        //Given  : Dữ liệu đầu vào đã biết trước, dự đoán được nó sẽ xảy ra
             //Request - Response
             // Ở dưới yêu cầu dạng String nên mình phải chuyển về dạng String
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
             //Mock Create User của UserService
        Mockito.when(petService.createPet(ArgumentMatchers.any()))
                        .thenReturn(petResponse);
        //When : Khi mình test cái gì
        // Then : Khi when xảy ra, mình expect điều gì
        mockMvc.perform(MockMvcRequestBuilders
                .post("http://localhost:8080/api/v1/pets")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("petId").value("b20c8a31409c"))
                .andExpect(MockMvcResultMatchers.jsonPath("petName").value("Guhalu"))
                .andExpect(MockMvcResultMatchers.jsonPath("petType").value("Dog"))
                .andExpect(MockMvcResultMatchers.jsonPath("petAge").value("Young"));
    }

    @Test
    void createPet_InvalidPetName_fail() throws Exception {
        //Given  : Dữ liệu đầu vào đã biết trước, dự đoán được nó sẽ xảy ra
        request.setPetName("Langbo");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        //When : Khi mình test cái gì, Then : Khi when xảy ra, mình expect điều gì
        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Invalid message key"));
    }


}
