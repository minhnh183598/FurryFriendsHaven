package org.demo.huyminh.Service;



import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Request.PetCreationRequest;
import org.demo.huyminh.DTO.Request.PetUpdateRequest;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    //CREATE PET
    public Pet createPet(@Valid PetCreationRequest request) {

        log.info("Service : Create Pet");

        if (petRepository.existsByPetName(request.getPetName()))
            throw new RuntimeException("PetName has been Used");
        Pet pet = new Pet();

        pet.setPetName(capitalizeFirstLetter(request.getPetName()));
        pet.setPetAge(request.getPetAge());
        pet.setPetType(request.getPetType());
        pet.setPetBreed(capitalizeFirstLetter(request.getPetBreed()));
        pet.setPetColor(request.getPetColor());
        pet.setPetDescription(capitalizeFirstLetter(request.getPetDescription()));
        pet.setPetSize(request.getPetSize());
        pet.setPetWeight(request.getPetWeight());
        pet.setPetGender(request.getPetGender());
        pet.setPetVaccin(request.getPetVaccin());
        pet.setPetStatus(request.getPetStatus());

        return petRepository.save(pet);
    }
    //In Hoa chu cai dau
    private String capitalizeFirstLetter(String letter){
        if(letter.isEmpty()){
            return letter;
        }
        String[] words = letter.toLowerCase().split(" ");
        StringBuilder capitalized = new StringBuilder();

        for(String word : words){
            if(word.length() > 0 ){
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return capitalized.toString().trim();
    }

    //Get Pets
    public List<Pet> getPets() {
        return petRepository.findAllByOrderByCreatedPetAtDesc();
    }

    //Get Pet By ID
    public Pet getPet(String petId) {
        return petRepository.findById(petId)
                .orElseGet(() -> {
                    System.out.println("Pet not existed");
                    return null;
                });
    }

    public Pet updatePet(String petId, PetUpdateRequest request) {
        Pet pet = getPet(petId);

        pet.setPetName(capitalizeFirstLetter(request.getPetName()));
        pet.setPetAge(request.getPetAge());
        pet.setPetType(request.getPetType());
        pet.setPetBreed(capitalizeFirstLetter(request.getPetBreed()));
        pet.setPetColor(request.getPetColor());
        pet.setPetDescription(capitalizeFirstLetter(request.getPetDescription()));
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
                                String petColor, String petVaccin,String petStatus, String keyword,
                                                                      String sortPets) {
        List<Pet> pets = petRepository.searchPets(petType, petAge, petGender,
                petColor, petVaccin,petStatus, keyword);
        switch (sortPets) {
            case "sortByWeight":
                pets.sort((p1, p2) -> Float.compare(p1.getPetWeight(), p2.getPetWeight()));
                break;
            case "sortByName":
                pets.sort((p1, p2) -> p1.getPetName().compareTo(p2.getPetName()));
                break;
            case "sortByDate":
                pets.sort((p1, p2) -> p2.getCreatedPetAt().compareTo(p1.getCreatedPetAt()));
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