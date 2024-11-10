package org.demo.huyminh.Service;

import jakarta.validation.Valid;
import org.demo.huyminh.DTO.Reponse.PetResponse;
import org.demo.huyminh.DTO.Request.PetCreationRequest;
import org.demo.huyminh.DTO.Request.PetUpdateRequest;
import org.demo.huyminh.Entity.Pet;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:55 PM
 */

public interface PetService {
    Pet createPet(@Valid PetCreationRequest request);

    List<Pet> getPets();

    Pet getPet(String petId);

    List<PetResponse> getAdoptedPetsByAdopterId(String adopterId);

    Pet updatePetStatus(String petId, PetUpdateRequest request);

    Pet updatePet(String petId, PetUpdateRequest request);

    void deletePet(String petId);

    List<Pet> searchPets(String petType, String petAge, String petGender,
                         String petColor, String petVaccin, String petStatus, String keyword,
                         String sortPets);

    List<Pet> sort6pets();
}
