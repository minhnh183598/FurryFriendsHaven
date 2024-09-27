package org.demo.huyminh.Service;



import org.demo.huyminh.DTO.Request.PetCreationRequest;
import org.demo.huyminh.DTO.Request.PetUpdateRequest;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    //CREATE PET
    public Pet createPet(PetCreationRequest request) {
        if (petRepository.existsByPetName(request.getPetName()))
            throw new RuntimeException("PetName has been Used");
        Pet pet = new Pet();

        pet.setPetName(request.getPetName());
        pet.setPetAge(request.getPetAge());
        pet.setPetType(request.getPetType());
        pet.setPetBreed(request.getPetBreed());
        pet.setPetColor(request.getPetColor());
        pet.setPetDescription(request.getPetDescription());
        pet.setPetSize(request.getPetSize());
        pet.setPetWeight(request.getPetWeight());
        pet.setPetGender(request.getPetGender());
        pet.setPetVaccin(request.getPetVaccin());
        pet.setPetStatus(request.getPetStatus());

        return petRepository.save(pet);
    }

    //Get Pets
    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public Pet getPet(String petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not existed"));
    }

    public Pet updatePet(String petId, PetUpdateRequest request) {
        Pet pet = getPet(petId);

        pet.setPetName(request.getPetName());
        pet.setPetAge(request.getPetAge());
        pet.setPetType(request.getPetType());
        pet.setPetBreed(request.getPetBreed());
        pet.setPetColor(request.getPetColor());
        pet.setPetDescription(request.getPetDescription());
        pet.setPetSize(request.getPetSize());
        pet.setPetWeight(request.getPetWeight());
        pet.setPetVaccin(request.getPetVaccin());
        pet.setPetGender(request.getPetGender());
        pet.setPetStatus(request.getPetStatus());

        return petRepository.save(pet);
    }

    public void deletePet(String petId) {
        petRepository.deleteById(petId);
    }

    //Search Pets By Many Fields
    public List<Pet> searchPets(String petType, String petAge, String petGender,
                                String petColor, String petVaccin, String keyword,
                                                                      String sortPets) {
        List<Pet> pets = petRepository.searchPets(petType, petAge, petGender,
                petColor, petVaccin, keyword);
        switch (sortPets) {
            case "sortByWeight":
                pets.sort((p1, p2) -> Float.compare(p1.getPetWeight(), p2.getPetWeight()));
                break;
            case "sortByName":
                pets.sort((p1, p2) -> p1.getPetName().compareTo(p2.getPetName()));
                break;
            case "sortByAge":
                pets.sort(Comparator.comparingInt(pet -> {
                    switch (pet.getPetAge()) {
                        case "Young":
                            return 1;
                        case "Full Grown":
                            return 2;
                        case "Old":
                            return 3;
                        default:
                            return 4;
                    }
                }));
                break;
        }
        return pets;
    }

    //Sort 6 pets available
    public List<Pet> sort6pets(){

        List<Pet> pets = petRepository.findByPetStatus("Available");
        Collections.sort(pets, Comparator.comparing(Pet::getPetName));
        List<Pet> PetList;
        if(pets.size() > 6){
            PetList = pets.subList(0,6);
        } else{
            return PetList = pets;
        }
        return PetList;
    }


//    //Search by name
//    public List<Pet> searchByPetName(String keyword) {
//        if(keyword != null) {
//            return petRepository.searchByName(keyword);
//        }
//            return petRepository.findAll();
//    }
//    //Search Pet v2
//    public List<Pet> searchPets(String petType, String petGender, String petAge, String petColor,String petStatus, String petVaccin) {
//        return petRepository.searchPets(petType, petGender, petAge, petColor,petStatus ,petVaccin);
//    }
//    //Sort Pets by Name
//    public List<Pet> sortPetByName(){
//        return petRepository.findAllByOrderByPetNameAsc();
//    }
//    //Sort Pets by Weight
//    public List<Pet> sortPetByWeight(){
//        return petRepository.findAllByOrderByPetWeight();
//    }
//    //Sort Pets by Age
//    public List<Pet> sortPetByAge(){
//        return petRepository.sortPetByAge();
//    }

//    //Find pet_status = adopted
//    public List<Pet> adoptedPet(){
//        return petRepository.findByPetStatus("Adopted");
//    }
//    //Find pet_status = available
//    public List<Pet> availablePet(){
//        return petRepository.findByPetStatus("Available");
//    }






}
