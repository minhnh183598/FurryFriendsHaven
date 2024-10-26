package org.demo.huyminh.Service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Repository.PetRepository;
import org.demo.huyminh.Service.ExcelService;
import org.demo.huyminh.Util.ExcelHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

/**
 * @author Minh
 * Date: 10/21/2024
 * Time: 9:44 AM
 */

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    final PetRepository petRepository;

    @Override
    public void save(MultipartFile file) {
        try {
            List<Pet> pets = ExcelHelper.excelToPet(file.getInputStream());
            for(Pet pet : pets) {
                if(!petRepository.existsByPetName(pet.getPetName())) {
                    petRepository.save(pet);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
